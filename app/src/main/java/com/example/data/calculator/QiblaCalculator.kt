package com.example.data.calculator

import kotlin.math.*

object QiblaCalculator {

    const val KAABA_LATITUDE = 21.422487
    const val KAABA_LONGITUDE = 39.826206

    /**
     * Calculates the Qibla bearing in degrees from North (0 - 360)
     */
    fun calculateQiblaBearing(userLat: Double, userLng: Double): Float {
        val userLatRad = Math.toRadians(userLat)
        val userLngRad = Math.toRadians(userLng)
        val kaabaLatRad = Math.toRadians(KAABA_LATITUDE)
        val kaabaLngRad = Math.toRadians(KAABA_LONGITUDE)

        val deltaLng = kaabaLngRad - userLngRad

        val y = sin(deltaLng) * cos(kaabaLatRad)
        val x = cos(userLatRad) * sin(kaabaLatRad) - sin(userLatRad) * cos(kaabaLatRad) * cos(deltaLng)

        var bearing = Math.toDegrees(atan2(y, x))
        if (bearing < 0) {
            bearing += 360.0
        }
        return bearing.toFloat()
    }

    /**
     * Calculates approximate distance to Makkah in Kilometers
     */
    fun calculateDistanceToKaabaKm(userLat: Double, userLng: Double): Int {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(KAABA_LATITUDE - userLat)
        val dLng = Math.toRadians(KAABA_LONGITUDE - userLng)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(userLat)) * cos(Math.toRadians(KAABA_LATITUDE)) *
                sin(dLng / 2) * sin(dLng / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadiusKm * c).roundToInt()
    }
}
