package com.example.data.calculator

import com.example.data.model.CalculationMethod
import com.example.data.model.JuristicMethod
import com.example.data.model.PrayerTimeInfo
import com.example.data.model.PrayerType
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.*

object PrayerTimeCalculator {

    // Default Kaaba Coordinates
    const val MAKKAH_LAT = 21.4225
    const val MAKKAH_LNG = 39.8262

    /**
     * Computes the 6 key prayer times for a specific date and coordinates
     */
    fun calculatePrayerTimes(
        calendar: Calendar,
        latitude: Double = 24.7136, // Default Riyadh / Makkah timezone area
        longitude: Double = 46.6753,
        timeZoneOffsetHours: Double = (calendar.timeZone.rawOffset + calendar.timeZone.dstSavings).toDouble() / (1000.0 * 3600.0),
        method: CalculationMethod = CalculationMethod.UMM_AL_QURA,
        juristicMethod: JuristicMethod = JuristicMethod.STANDARD,
        manualOffsetMinutes: Map<PrayerType, Int> = emptyMap()
    ): List<PrayerTimeInfo> {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val julianDate = getJulianDate(year, month, day) - longitude / (15.0 * 24.0)

        // Sun declination and equation of time
        val d = julianDate - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(Math.toRadians(g)) + 0.020 * sin(Math.toRadians(2 * g)))

        val e = 23.439 - 0.00000036 * d
        val declination = Math.toDegrees(asin(sin(Math.toRadians(e)) * sin(Math.toRadians(l))))
        val equationOfTime = (q / 15.0) - fixHour(Math.toDegrees(atan2(cos(Math.toRadians(e)) * sin(Math.toRadians(l)), cos(Math.toRadians(l)))) / 15.0)

        // Mid-Day (Dhuhr)
        val dhuhrBase = 12.0 + timeZoneOffsetHours - (longitude / 15.0) - (equationOfTime * 1.0)
        val dhuhrHour = fixHour(dhuhrBase)

        // Sunrise & Sunset angle calculations (0.833° for refraction and sun radius)
        val sunAlt = -0.833
        val sunriseHour = dhuhrHour - hourAngle(latitude, declination, sunAlt) / 15.0
        val sunsetHour = dhuhrHour + hourAngle(latitude, declination, sunAlt) / 15.0

        // Fajr calculation
        val fajrAngle = -method.fajrAngle
        val fajrHour = dhuhrHour - hourAngle(latitude, declination, fajrAngle) / 15.0

        // Asr calculation
        val asrFactor = if (juristicMethod == JuristicMethod.HANAFI) 2.0 else 1.0
        val asrAngle = -Math.toDegrees(atan(1.0 / (asrFactor + tan(Math.toRadians(abs(latitude - declination))))))
        val asrHour = dhuhrHour + hourAngle(latitude, declination, asrAngle) / 15.0

        // Maghrib calculation (same as sunset or slight offset)
        val maghribHour = sunsetHour

        // Isha calculation
        val ishaHour = if (method == CalculationMethod.UMM_AL_QURA) {
            maghribHour + 1.5 // 90 minutes after Maghrib
        } else {
            val ishaAngle = -method.ishaAngle
            dhuhrHour + hourAngle(latitude, declination, ishaAngle) / 15.0
        }

        val prayerHoursMap = mapOf(
            PrayerType.FAJR to fajrHour,
            PrayerType.SUNRISE to sunriseHour,
            PrayerType.DHUHR to dhuhrHour,
            PrayerType.ASR to asrHour,
            PrayerType.MAGHRIB to maghribHour,
            PrayerType.ISHA to ishaHour
        )

        val nowMillis = System.currentTimeMillis()
        val calendarClone = calendar.clone() as Calendar

        // Build list of times
        val times = mutableListOf<PrayerTimeInfo>()
        var foundNext = false

        for (type in PrayerType.values()) {
            val rawHour = prayerHoursMap[type] ?: 12.0
            val offset = manualOffsetMinutes[type] ?: 0

            val totalMinutes = (rawHour * 60.0).roundToInt() + offset
            val hours = (totalMinutes / 60) % 24
            val minutes = totalMinutes % 60

            calendarClone.set(Calendar.HOUR_OF_DAY, hours)
            calendarClone.set(Calendar.MINUTE, minutes)
            calendarClone.set(Calendar.SECOND, 0)
            calendarClone.set(Calendar.MILLISECOND, 0)

            val pMillis = calendarClone.timeInMillis
            val isPast = pMillis < nowMillis
            val isNext = !isPast && !foundNext && type != PrayerType.SUNRISE

            if (isNext) {
                foundNext = true
            }

            val remaining = if (pMillis > nowMillis) pMillis - nowMillis else 0L

            times.add(
                PrayerTimeInfo(
                    type = type,
                    timeFormatted = String.format("%02d:%02d", hours, minutes),
                    hours = hours,
                    minutes = minutes,
                    timestampMillis = pMillis,
                    isPast = isPast,
                    isNext = isNext,
                    remainingMillis = remaining
                )
            )
        }

        // If all prayers today are past, next is Fajr of tomorrow
        if (!foundNext) {
            val fajrIndex = times.indexOfFirst { it.type == PrayerType.FAJR }
            if (fajrIndex != -1) {
                val fajr = times[fajrIndex]
                val tomorrowFajrMillis = fajr.timestampMillis + 24 * 3600 * 1000
                times[fajrIndex] = fajr.copy(
                    isNext = true,
                    remainingMillis = tomorrowFajrMillis - nowMillis
                )
            }
        }

        return times
    }

    private fun getJulianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun hourAngle(lat: Double, decl: Double, alt: Double): Double {
        val cosHA = (sin(Math.toRadians(alt)) - sin(Math.toRadians(lat)) * sin(Math.toRadians(decl))) /
                (cos(Math.toRadians(lat)) * cos(Math.toRadians(decl)))
        val clampedCos = cosHA.coerceIn(-1.0, 1.0)
        return Math.toDegrees(acos(clampedCos))
    }

    private fun fixAngle(angle: Double): Double {
        var a = angle - 360.0 * floor(angle / 360.0)
        if (a < 0) a += 360.0
        return a
    }

    private fun fixHour(hour: Double): Double {
        var h = hour - 24.0 * floor(hour / 24.0)
        if (h < 0) h += 24.0
        return h
    }
}
