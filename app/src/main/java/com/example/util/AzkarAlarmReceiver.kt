package com.example.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AzkarAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return
        val action = intent.action ?: return

        when (action) {
            AzkarNotificationScheduler.ACTION_AZKAR_REMINDER -> {
                val isMorning = intent.getBooleanExtra(AzkarNotificationScheduler.EXTRA_IS_MORNING, true)
                AzkarNotificationScheduler.showAzkarNotification(context, isMorning)
                // Automatically reschedule for the next day to maintain daily consistency
                AzkarNotificationScheduler.rescheduleNextOccurrence(context, isMorning)
            }
            Intent.ACTION_BOOT_COMPLETED, "android.intent.action.QUICKBOOT_POWERON" -> {
                AzkarNotificationScheduler.scheduleAllConfigured(context)
            }
        }
    }
}
