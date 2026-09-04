package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.AzkarNotificationScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("نُور", appName)
  }

  @Test
  fun `test azkar notification preference saving and scheduling`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    AzkarNotificationScheduler.scheduleReminder(context, isMorning = true, hour = 6, minute = 45)
    val prefs = AzkarNotificationScheduler.getPrefs(context)
    assertTrue(prefs.getBoolean(AzkarNotificationScheduler.KEY_MORNING_ENABLED, false))
    assertEquals(6, prefs.getInt(AzkarNotificationScheduler.KEY_MORNING_HOUR, 0))
    assertEquals(45, prefs.getInt(AzkarNotificationScheduler.KEY_MORNING_MINUTE, 0))

    AzkarNotificationScheduler.scheduleReminder(context, isMorning = false, hour = 18, minute = 15)
    assertTrue(prefs.getBoolean(AzkarNotificationScheduler.KEY_EVENING_ENABLED, false))
    assertEquals(18, prefs.getInt(AzkarNotificationScheduler.KEY_EVENING_HOUR, 0))
    assertEquals(15, prefs.getInt(AzkarNotificationScheduler.KEY_EVENING_MINUTE, 0))
  }

  @Test
  fun `test azkar streak incrementation on completion`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val initialStreak = AzkarNotificationScheduler.getStreakCount(context)
    val updatedStreak = AzkarNotificationScheduler.recordAzkarSessionCompleted(context)
    assertTrue(updatedStreak >= 1)
  }
}

