package com.example.data.calculator

import com.example.data.model.IslamicOccasion
import java.util.Calendar
import kotlin.math.floor

object HijriCalendarHelper {

    private val HIJRI_MONTHS_ARABIC = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    data class HijriDate(
        val day: Int,
        val month: Int, // 1-12
        val monthName: String,
        val year: Int,
        val formatted: String
    )

    /**
     * Converts a Gregorian Calendar to approximate Hijri date
     */
    fun getHijriDate(calendar: Calendar, adjustmentDays: Int = 0): HijriDate {
        val cal = calendar.clone() as Calendar
        cal.add(Calendar.DAY_OF_MONTH, adjustmentDays)

        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)

        // Standard Kuweit / Astronomical algorithm for Hijri conversion
        val jd = getJulianDay(y, m, d)
        val l = (jd - 1948440.0 + 10632.0)
        val n = floor((l - 1.0) / 10631.0).toInt()
        val lPrime = l - 10631.0 * n + 354.0
        val j = (floor((10985.0 - lPrime) / 5316.0) * floor((50.0 * lPrime + 245.0) / 17719.0) +
                floor(lPrime / 5670.0) * floor((43.0 * lPrime + 152.0) / 15238.0)).toInt()
        val lSecond = (lPrime - floor((30.0 - j) / 15.0) * floor((17719.0 * j / 50.0)) -
                floor(j / 16.0) * floor((15238.0 * j / 43.0)) + 29.0)
        val month = floor((24.0 * lSecond) / 709.0).toInt()
        val day = (lSecond - floor((709.0 * month) / 24.0)).toInt()
        val year = 30 * n + j - 30

        val safeMonth = month.coerceIn(1, 12)
        val monthName = HIJRI_MONTHS_ARABIC[safeMonth - 1]
        val formatted = "$day $monthName $year هـ"

        return HijriDate(
            day = day,
            month = safeMonth,
            monthName = monthName,
            year = year,
            formatted = formatted
        )
    }

    private fun getJulianDay(year: Int, month: Int, day: Int): Double {
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

    fun isWhiteDays(hijriDay: Int): Boolean {
        return hijriDay in 13..15
    }

    fun getUpcomingOccasions(currentHijri: HijriDate): List<IslamicOccasion> {
        return listOf(
            IslamicOccasion(
                titleArabic = "شهر رمضان المبارك 🌙",
                hijriDate = "1 رمضان",
                gregorianDateApprox = "بداية شهر الصيام والقرآن",
                description = "شهر الرحمة والمغفرة والعتق من النيران، وفيه ليلة القدر خير من ألف شهر.",
                isFastingRecommended = true
            ),
            IslamicOccasion(
                titleArabic = "عيد الفطر المبارك",
                hijriDate = "1 شوال",
                gregorianDateApprox = "أول أيام عيد الفطر",
                description = "يوم الجائزة والفرح بإتمام صيام شهر رمضان وزكاة الفطر.",
                isFastingRecommended = false
            ),
            IslamicOccasion(
                titleArabic = "عشر ذي الحجة ويوم عرفة",
                hijriDate = "1 - 9 ذو الحجة",
                gregorianDateApprox = "أفضل أيام الدنيا ويوم عرفة",
                description = "العمل الصالح فيها أحب إلى الله، وصيام يوم عرفة يكفر سنة ماضية وسنة باقية.",
                isFastingRecommended = true
            ),
            IslamicOccasion(
                titleArabic = "عيد الأضحى المبارك 🕋",
                hijriDate = "10 ذو الحجة",
                gregorianDateApprox = "يوم النحر وأيام التشريق",
                description = "يوم الحج الأكبر وذبح الأضاحي وذكر الله تعالى في الأيام المعدودات.",
                isFastingRecommended = false
            ),
            IslamicOccasion(
                titleArabic = "يوم عاشوراء (10 محرم)",
                hijriDate = "10 محرم",
                gregorianDateApprox = "نجاة موسى عليه السلام",
                description = "صيامه يكفر ذنوب السنة الماضية، ويستحب صيام التاسع معه.",
                isFastingRecommended = true
            ),
            IslamicOccasion(
                titleArabic = "صيام الأيام البيض (13، 14، 15 من كل شهر)",
                hijriDate = "13 - 15 من كل شهر هجري",
                gregorianDateApprox = "سنة مؤكدة شهرياً",
                description = "صيام ثلاثة أيام من كل شهر كصيام الدهر كله.",
                isFastingRecommended = true
            )
        )
    }
}
