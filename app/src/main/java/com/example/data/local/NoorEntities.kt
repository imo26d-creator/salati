package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_logs")
data class PrayerLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateString: String, // Format: YYYY-MM-DD
    val prayerName: String, // Fajr, Dhuhr, Asr, Maghrib, Isha
    val status: String, // ON_TIME, LATE, MISSED
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "quran_progress")
data class QuranProgressEntity(
    @PrimaryKey val dateString: String, // YYYY-MM-DD
    val pagesRead: Int = 0,
    val targetPages: Int = 5,
    val lastSurahNumber: Int = 1,
    val lastAyahNumber: Int = 1,
    val lastSurahName: String = "الفاتحة"
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val ayahText: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbih_records")
data class TasbihRecordEntity(
    @PrimaryKey val dhikrName: String,
    val count: Int = 0,
    val dailyTarget: Int = 100,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_checklist")
data class DailyChecklistEntity(
    @PrimaryKey val dateString: String,
    val morningAzkarDone: Boolean = false,
    val eveningAzkarDone: Boolean = false,
    val sleepAzkarDone: Boolean = false,
    val duhaPrayerDone: Boolean = false,
    val witrPrayerDone: Boolean = false,
    val charityDone: Boolean = false
)
