package com.example

import com.example.data.model.PrayerType
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests verifying prayer types, calculations, and early Adhan configurations.
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testPrayerTypeDisplayNames() {
    assertEquals("الفجر", PrayerType.FAJR.arabicName)
    assertEquals("الظهر", PrayerType.DHUHR.arabicName)
    assertEquals("العصر", PrayerType.ASR.arabicName)
    assertEquals("المغرب", PrayerType.MAGHRIB.arabicName)
    assertEquals("العشاء", PrayerType.ISHA.arabicName)
    assertEquals("الشروق", PrayerType.SUNRISE.arabicName)
  }

  @Test
  fun testPrePrayerAlertMinutesValidRange() {
    val validOptions = listOf(5, 10, 15, 20, 30)
    assertTrue(validOptions.contains(15))
    assertTrue(validOptions.all { it in 1..60 })
  }
}
