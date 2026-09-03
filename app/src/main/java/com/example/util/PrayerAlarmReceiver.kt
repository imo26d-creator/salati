package com.example.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.data.model.AzanAlertType
import com.example.data.model.MuezzinVoice
import com.example.data.model.PrayerType

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        val action = intent.action ?: return

        when (action) {
            PrayerNotificationScheduler.ACTION_PRAYER_AZAN -> {
                handlePrayerAzan(context, intent)
            }
            PrayerNotificationScheduler.ACTION_PRE_PRAYER -> {
                handlePrePrayerAlert(context, intent)
            }
            Intent.ACTION_BOOT_COMPLETED, "android.intent.action.QUICKBOOT_POWERON" -> {
                // Device rebooted; channels created and ready
                PrayerNotificationScheduler.createNotificationChannels(context)
            }
        }
    }

    private fun handlePrayerAzan(context: Context, intent: Intent) {
        val prayerTypeName = intent.getStringExtra(PrayerNotificationScheduler.EXTRA_PRAYER_TYPE) ?: PrayerType.DHUHR.name
        val muezzinName = intent.getStringExtra(PrayerNotificationScheduler.EXTRA_MUEZZIN_NAME) ?: MuezzinVoice.MAKKAH.name
        val alertTypeName = intent.getStringExtra(PrayerNotificationScheduler.EXTRA_ALERT_TYPE) ?: AzanAlertType.FULL_AZAN.name
        val volume = intent.getFloatExtra(PrayerNotificationScheduler.EXTRA_VOLUME, 0.85f)

        val prayerType = try {
            PrayerType.valueOf(prayerTypeName)
        } catch (_: Exception) {
            PrayerType.DHUHR
        }

        val muezzin = try {
            MuezzinVoice.valueOf(muezzinName)
        } catch (_: Exception) {
            MuezzinVoice.MAKKAH
        }

        val alertType = try {
            AzanAlertType.valueOf(alertTypeName)
        } catch (_: Exception) {
            AzanAlertType.FULL_AZAN
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            prayerType.ordinal + 500,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "🕌 حان الآن وقت أذان صلاة ${prayerType.arabicName}"
        val content = when (alertType) {
            AzanAlertType.FULL_AZAN -> "حي على الصلاة، حي على الفلاح • أذان بصوت ${muezzin.titleArabic}"
            AzanAlertType.TAKBEER_ONLY -> "الله أكبر، الله أكبر • تكبيرات الأذان بصوت ${muezzin.titleArabic}"
            AzanAlertType.RECITER_VOICE -> "تلاوة قرآنية خاشعة • حان موعد صلاة ${prayerType.arabicName}"
            AzanAlertType.VIBRATE_ONLY -> "حان موعد صلاة ${prayerType.arabicName} (تنبيه بالاهتزاز)"
            AzanAlertType.SILENT -> "حان موعد صلاة ${prayerType.arabicName} (إشعار صامت)"
        }

        val notification = NotificationCompat.Builder(context, PrayerNotificationScheduler.CHANNEL_AZAN_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .apply {
                if (alertType != AzanAlertType.SILENT) {
                    setVibrate(longArrayOf(0, 500, 250, 500))
                }
            }
            .build()

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(prayerType.ordinal + 1000, notification)
        } catch (_: SecurityException) {}

        // Handle Audio and Vibration (Strictly free from any musical instruments)
        when (alertType) {
            AzanAlertType.FULL_AZAN -> {
                AzanSoundPlayer.playMuezzinPreview(muezzin, volume, prayerType)
            }
            AzanAlertType.TAKBEER_ONLY -> {
                AzanSoundPlayer.playTakbeerAlert(volume)
            }
            AzanAlertType.RECITER_VOICE -> {
                AzanSoundPlayer.playReciterAyahAlert(volume)
            }
            AzanAlertType.VIBRATE_ONLY -> {
                triggerVibration(context)
            }
            AzanAlertType.SILENT -> {}
        }
    }

    private fun handlePrePrayerAlert(context: Context, intent: Intent) {
        val prayerTypeName = intent.getStringExtra(PrayerNotificationScheduler.EXTRA_PRAYER_TYPE) ?: PrayerType.DHUHR.name
        val preMinutes = intent.getIntExtra(PrayerNotificationScheduler.EXTRA_PRE_MINUTES, 5)
        val volume = intent.getFloatExtra(PrayerNotificationScheduler.EXTRA_VOLUME, 0.7f)

        val prayerType = try {
            PrayerType.valueOf(prayerTypeName)
        } catch (_: Exception) {
            PrayerType.DHUHR
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            prayerType.ordinal + 600,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "⏳ اقتراب وقت صلاة ${prayerType.arabicName}"
        val content = "بقي قرابة $preMinutes دقائق على موعد أذان ${prayerType.arabicName} • استعد للوضوء وإدراك تكبيرة الإحرام"

        val notification = NotificationCompat.Builder(context, PrayerNotificationScheduler.CHANNEL_PRE_PRAYER_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 300, 200, 300))
            .build()

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(prayerType.ordinal + 2000, notification)
        } catch (_: SecurityException) {}

        // Play Takbeer alert for pre-prayer alert (No music)
        AzanSoundPlayer.playTakbeerAlert(volume = volume)
    }

    private fun triggerVibration(context: Context) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 400, 200, 400), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 400, 200, 400), -1)
            }
        } catch (_: Exception) {}
    }
}
