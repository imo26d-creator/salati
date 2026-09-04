package com.example.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object AzkarNotificationScheduler {

    const val ACTION_AZKAR_REMINDER = "com.example.ACTION_AZKAR_REMINDER"
    const val EXTRA_IS_MORNING = "extra_is_morning"

    const val CHANNEL_AZKAR_ID = "channel_azkar_reminders"
    private const val CHANNEL_NAME = "أذكار الصباح والمساء"

    private const val REQUEST_CODE_MORNING = 2001
    private const val REQUEST_CODE_EVENING = 2002
    private const val NOTIFICATION_ID_MORNING = 3001
    private const val NOTIFICATION_ID_EVENING = 3002

    private const val PREFS_NAME = "azkar_notification_prefs"
    const val KEY_MORNING_ENABLED = "pref_morning_enabled"
    const val KEY_MORNING_HOUR = "pref_morning_hour"
    const val KEY_MORNING_MINUTE = "pref_morning_minute"
    const val KEY_EVENING_ENABLED = "pref_evening_enabled"
    const val KEY_EVENING_HOUR = "pref_evening_hour"
    const val KEY_EVENING_MINUTE = "pref_evening_minute"
    const val KEY_STREAK_COUNT = "pref_azkar_streak_count"
    const val KEY_LAST_COMPLETED_DATE = "pref_azkar_last_completed_date"

    fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
            val channel = NotificationChannel(
                CHANNEL_AZKAR_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "تنبيهات يومية منتظمة لأذكار الصباح وأذكار المساء لتعزيز الاستمرارية"
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Schedules both morning and evening Azkar alarms according to stored user preferences.
     */
    fun scheduleAllConfigured(context: Context) {
        val prefs = getPrefs(context)
        val morningEnabled = prefs.getBoolean(KEY_MORNING_ENABLED, true)
        val morningHour = prefs.getInt(KEY_MORNING_HOUR, 6)
        val morningMinute = prefs.getInt(KEY_MORNING_MINUTE, 30)

        val eveningEnabled = prefs.getBoolean(KEY_EVENING_ENABLED, true)
        val eveningHour = prefs.getInt(KEY_EVENING_HOUR, 17)
        val eveningMinute = prefs.getInt(KEY_EVENING_MINUTE, 30)

        if (morningEnabled) {
            scheduleReminder(context, isMorning = true, morningHour, morningMinute)
        } else {
            cancelReminder(context, isMorning = true)
        }

        if (eveningEnabled) {
            scheduleReminder(context, isMorning = false, eveningHour, eveningMinute)
        } else {
            cancelReminder(context, isMorning = false)
        }
    }

    /**
     * Schedules an alarm for morning or evening Azkar at the given hour and minute.
     */
    fun scheduleReminder(
        context: Context,
        isMorning: Boolean,
        hour: Int,
        minute: Int
    ) {
        createNotificationChannel(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        // Save into prefs
        val prefs = getPrefs(context)
        prefs.edit().apply {
            if (isMorning) {
                putBoolean(KEY_MORNING_ENABLED, true)
                putInt(KEY_MORNING_HOUR, hour)
                putInt(KEY_MORNING_MINUTE, minute)
            } else {
                putBoolean(KEY_EVENING_ENABLED, true)
                putInt(KEY_EVENING_HOUR, hour)
                putInt(KEY_EVENING_MINUTE, minute)
            }
            apply()
        }

        val targetCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val now = Calendar.getInstance()
        // If the scheduled time for today has already passed, schedule for tomorrow
        if (targetCal.before(now) || targetCal.timeInMillis <= now.timeInMillis) {
            targetCal.add(Calendar.DAY_OF_YEAR, 1)
        }

        val intent = Intent(context, AzkarAlarmReceiver::class.java).apply {
            action = ACTION_AZKAR_REMINDER
            putExtra(EXTRA_IS_MORNING, isMorning)
        }

        val requestCode = if (isMorning) REQUEST_CODE_MORNING else REQUEST_CODE_EVENING
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        targetCal.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        targetCal.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    targetCal.timeInMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                targetCal.timeInMillis,
                pendingIntent
            )
        }
    }

    /**
     * Cancels the reminder for morning or evening.
     */
    fun cancelReminder(context: Context, isMorning: Boolean) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, AzkarAlarmReceiver::class.java).apply {
            action = ACTION_AZKAR_REMINDER
            putExtra(EXTRA_IS_MORNING, isMorning)
        }
        val requestCode = if (isMorning) REQUEST_CODE_MORNING else REQUEST_CODE_EVENING
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)

        val prefs = getPrefs(context)
        prefs.edit().apply {
            if (isMorning) {
                putBoolean(KEY_MORNING_ENABLED, false)
            } else {
                putBoolean(KEY_EVENING_ENABLED, false)
            }
            apply()
        }
    }

    /**
     * Automatically called by AzkarAlarmReceiver after firing an alarm to ensure
     * daily continuity for subsequent days.
     */
    fun rescheduleNextOccurrence(context: Context, isMorning: Boolean) {
        val prefs = getPrefs(context)
        val isEnabled = if (isMorning) {
            prefs.getBoolean(KEY_MORNING_ENABLED, true)
        } else {
            prefs.getBoolean(KEY_EVENING_ENABLED, true)
        }
        if (!isEnabled) return

        val hour = if (isMorning) {
            prefs.getInt(KEY_MORNING_HOUR, 6)
        } else {
            prefs.getInt(KEY_EVENING_HOUR, 17)
        }
        val minute = if (isMorning) {
            prefs.getInt(KEY_MORNING_MINUTE, 30)
        } else {
            prefs.getInt(KEY_EVENING_MINUTE, 30)
        }

        scheduleReminder(context, isMorning, hour, minute)
    }

    /**
     * Builds and displays the notification for morning or evening Azkar.
     */
    fun showAzkarNotification(context: Context, isMorning: Boolean) {
        createNotificationChannel(context)

        val title = if (isMorning) {
            "☀️ حان وقت أذكار الصباح المباركة"
        } else {
            "🌙 حان وقت أذكار المساء المباركة"
        }

        val contentText = if (isMorning) {
            "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ... ابدأ نهارك بذكر الله وانعم بالبركة والسكينة وحفظ الرحمن."
        } else {
            "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ... اختم يومك بحصن المسلم واطمئن بذكر الله قبل انقضاء النهار."
        }

        val categoryExtra = if (isMorning) "MORNING" else "EVENING"

        // Main Click Intent -> Opens AzkarScreen directly with the appropriate category
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("open_tab", "azkar")
            putExtra("azkar_category", categoryExtra)
        }
        val mainPendingIntent = PendingIntent.getActivity(
            context,
            if (isMorning) 4001 else 4002,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 1: "قراءة الأذكار الآن"
        val readIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("open_tab", "azkar")
            putExtra("azkar_category", categoryExtra)
        }
        val readPendingIntent = PendingIntent.getActivity(
            context,
            if (isMorning) 4003 else 4004,
            readIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 2: "السبحة الذكية 📿"
        val tasbihIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("open_tab", "azkar")
            putExtra("azkar_tab_index", 1)
        }
        val tasbihPendingIntent = PendingIntent.getActivity(
            context,
            if (isMorning) 4005 else 4006,
            tasbihIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_AZKAR_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(mainPendingIntent)
            .setAutoCancel(true)
            .setSubText("حصن المسلم")
            .addAction(0, "قراءة الأذكار 📖", readPendingIntent)
            .addAction(0, "السبحة الذكية 📿", tasbihPendingIntent)
            .build()

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            val notificationId = if (isMorning) NOTIFICATION_ID_MORNING else NOTIFICATION_ID_EVENING
            notificationManager.notify(notificationId, notification)
        } catch (_: SecurityException) {}
    }

    /**
     * Instantly sends a test notification so the user can verify the sound, look, and timing.
     */
    fun sendInstantTestNotification(context: Context, isMorning: Boolean) {
        showAzkarNotification(context, isMorning)
    }

    /**
     * Records completion of an Azkar session, tracking streak consistency.
     */
    fun recordAzkarSessionCompleted(context: Context): Int {
        val prefs = getPrefs(context)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val today = dateFormat.format(Calendar.getInstance().time)
        val lastDate = prefs.getString(KEY_LAST_COMPLETED_DATE, null)
        var streak = prefs.getInt(KEY_STREAK_COUNT, 3)

        if (lastDate != today) {
            val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
            val yesterday = dateFormat.format(yesterdayCal.time)

            streak = if (lastDate == yesterday) {
                streak + 1
            } else if (lastDate == null) {
                1
            } else {
                // missed a day, restart at 1
                1
            }
            prefs.edit()
                .putString(KEY_LAST_COMPLETED_DATE, today)
                .putInt(KEY_STREAK_COUNT, streak)
                .apply()
        }
        return streak
    }

    fun getStreakCount(context: Context): Int {
        return getPrefs(context).getInt(KEY_STREAK_COUNT, 3)
    }
}
