package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoorDao {
    // Prayer Logs
    @Query("SELECT * FROM prayer_logs WHERE dateString = :dateString")
    fun getPrayerLogsForDate(dateString: String): Flow<List<PrayerLogEntity>>

    @Query("SELECT * FROM prayer_logs ORDER BY timestamp DESC LIMIT 35")
    fun getRecentPrayerLogs(): Flow<List<PrayerLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerLog(log: PrayerLogEntity)

    @Query("DELETE FROM prayer_logs WHERE dateString = :dateString AND prayerName = :prayerName")
    suspend fun deletePrayerLog(dateString: String, prayerName: String)

    // Quran Progress
    @Query("SELECT * FROM quran_progress WHERE dateString = :dateString")
    fun getQuranProgress(dateString: String): Flow<QuranProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuranProgress(progress: QuranProgressEntity)

    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkEntity)

    // Tasbih Records
    @Query("SELECT * FROM tasbih_records")
    fun getAllTasbihRecords(): Flow<List<TasbihRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTasbihRecord(record: TasbihRecordEntity)

    // Daily Checklist
    @Query("SELECT * FROM daily_checklist WHERE dateString = :dateString")
    fun getDailyChecklist(dateString: String): Flow<DailyChecklistEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDailyChecklist(checklist: DailyChecklistEntity)
}
