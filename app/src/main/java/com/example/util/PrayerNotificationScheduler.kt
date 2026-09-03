package com.example.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.data.model.AzanAlertType
import com.example.data.model.MuezzinVoice
import com.example.data.model.PrayerAzanConfig
import com.example.data.model.PrayerTimeInfo
import com.example.data.model.PrayerType
import java.util.Calendar

object PrayerNotificationScheduler {

    const val CHANNEL_AZAN_ID = "noor_azan_channel"
    const val CHANNEL_PRE_PRAYER_ID = "noor_pre_prayer_channel"

    const val ACTION_PRAYER_AZAN = "com.example.ACTION_PRAYER_AZAN"
    const val ACTION_PRE_PRAYER = "com.example.ACTION_PRE_PRAYER"

    const val EXTRA_PRAYER_TYPE = "extra_prayer_type"
    const val EXTRA_MUEZZIN_NAME = "extra_muezzin_name"
    const val EXTRA_ALERT_TYPE = "extra_alert_type"
    const val EXTRA_VOLUME = "extra_volume"
    const val EXTRA_PRE_MINUTES = "extra_pre_minutes"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

            // Channel for Main Azan & Prayer alerts
            val azanChannel = NotificationChannel(
                CHANNEL_AZAN_ID,
                "تنبيهات الأذان ودخول أوقات الصلاة",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "إشعار وتنبيه صوتي ومرئي عند حلول وقت كل صلاة من الصلوات الخمس"
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
                setBypassDnd(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            // Channel for Pre-Prayer alerts (3-15 min before)
            val prePrayerChannel = NotificationChannel(
                CHANNEL_PRE_PRAYER_ID,
                "التنبيه المسبق قبل دخول وقت الصلاة",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "تنبيه هادئ للاستعداد والوضوء قبل رفع أذان الصلاة ببضع دقائق"
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
            }

            notificationManager.createNotificationChannel(azanChannel)
            notificationManager.createNotificationChannel(prePrayerChannel)
        }
    }

    fun scheduleAllPrayerAlarms(
        context: Context,
        prayerTimes: List<PrayerTimeInfo>,
        configs: Map<PrayerType, PrayerAzanConfig>,
        preAlertMinutes: Int = 0
    ) {
        createNotificationChannels(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val now = System.currentTimeMillis()

        for (prayerItem in prayerTimes) {
            val config = configs[prayerItem.type] ?: PrayerAzanConfig(prayerType = prayerItem.type)
            val prayerTimeMs = prayerItem.timestampMillis

            // Main Azan Alarm
            if (config.isEnabled && prayerTimeMs > now) {
                val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                    action = ACTION_PRAYER_AZAN
                    putExtra(EXTRA_PRAYER_TYPE, prayerItem.type.name)
                    putExtra(EXTRA_MUEZZIN_NAME, config.muezzin.name)
                    putExtra(EXTRA_ALERT_TYPE, config.alertType.name)
                    putExtra(EXTRA_VOLUME, config.volume)
                }

                val requestCode = prayerItem.type.ordinal * 10
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                scheduleExactOrApproximate(alarmManager, prayerTimeMs, pendingIntent)
            }

            // Pre-Prayer Alarm
            if (config.isEnabled && preAlertMinutes > 0) {
                val preAlertTimeMs = prayerTimeMs - (preAlertMinutes * 60 * 1000L)
                if (preAlertTimeMs > now) {
                    val preIntent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                        action = ACTION_PRE_PRAYER
                        putExtra(EXTRA_PRAYER_TYPE, prayerItem.type.name)
                        putExtra(EXTRA_PRE_MINUTES, preAlertMinutes)
                        putExtra(EXTRA_VOLUME, config.volume)
                    }

                    val preRequestCode = prayerItem.type.ordinal * 10 + 1
                    val prePendingIntent = PendingIntent.getBroadcast(
                        context,
                        preRequestCode,
                        preIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    scheduleExactOrApproximate(alarmManager, preAlertTimeMs, prePendingIntent)
                }
            }
        }
    }

    private fun scheduleExactOrApproximate(
        alarmManager: AlarmManager,
        triggerAtMillis: Long,
        pendingIntent: PendingIntent
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            // Fallback for cases where exact alarm permission isn't granted
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } catch (_: Exception) {}
    }

    fun sendInstantTestNotification(
        context: Context,
        prayerType: PrayerType = PrayerType.DHUHR,
        muezzin: MuezzinVoice = MuezzinVoice.MAKKAH,
        alertType: AzanAlertType = AzanAlertType.FULL_AZAN,
        volume: Float = 0.85f
    ) {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            9999,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_AZAN_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("🕌 حان الآن موعد أذان صلاة ${prayerType.arabicName}")
            .setContentText("حي على الصلاة، حي على الفلاح • بصوت ${muezzin.titleArabic}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("حان الآن موعد أذان صلاة ${prayerType.arabicName} المباركة.\nحي على الصلاة، حي على الفلاح.\nصوت الأذان: ${muezzin.titleArabic} (${muezzin.location}).")
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .build()

        try {
            val manager = NotificationManagerCompat.from(context)
            manager.notify(prayerType.ordinal + 100, notification)
        } catch (_: SecurityException) {}

        // Trigger Audio preview (Free from any musical instruments)
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
            AzanAlertType.VIBRATE_ONLY, AzanAlertType.SILENT -> {}
        }
    }
}
