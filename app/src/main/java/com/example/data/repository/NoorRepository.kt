package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow

class NoorRepository(private val dao: NoorDao) {

    fun getPrayerLogsForDate(dateString: String): Flow<List<PrayerLogEntity>> {
        return dao.getPrayerLogsForDate(dateString)
    }

    fun getRecentPrayerLogs(): Flow<List<PrayerLogEntity>> {
        return dao.getRecentPrayerLogs()
    }

    suspend fun savePrayerLog(dateString: String, prayerName: String, status: String) {
        dao.insertPrayerLog(
            PrayerLogEntity(
                dateString = dateString,
                prayerName = prayerName,
                status = status,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun getQuranProgress(dateString: String): Flow<QuranProgressEntity?> {
        return dao.getQuranProgress(dateString)
    }

    suspend fun saveQuranProgress(progress: QuranProgressEntity) {
        dao.saveQuranProgress(progress)
    }

    fun getAllBookmarks(): Flow<List<BookmarkEntity>> {
        return dao.getAllBookmarks()
    }

    suspend fun addBookmark(bookmark: BookmarkEntity) {
        dao.insertBookmark(bookmark)
    }

    suspend fun removeBookmark(bookmark: BookmarkEntity) {
        dao.deleteBookmark(bookmark)
    }

    fun getAllTasbihRecords(): Flow<List<TasbihRecordEntity>> {
        return dao.getAllTasbihRecords()
    }

    suspend fun saveTasbihRecord(dhikrName: String, count: Int, target: Int = 100) {
        dao.saveTasbihRecord(
            TasbihRecordEntity(
                dhikrName = dhikrName,
                count = count,
                dailyTarget = target,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    fun getDailyChecklist(dateString: String): Flow<DailyChecklistEntity?> {
        return dao.getDailyChecklist(dateString)
    }

    suspend fun saveDailyChecklist(checklist: DailyChecklistEntity) {
        dao.saveDailyChecklist(checklist)
    }
}
