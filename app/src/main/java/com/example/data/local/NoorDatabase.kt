package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PrayerLogEntity::class,
        QuranProgressEntity::class,
        BookmarkEntity::class,
        TasbihRecordEntity::class,
        DailyChecklistEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NoorDatabase : RoomDatabase() {
    abstract fun noorDao(): NoorDao

    companion object {
        @Volatile
        private var INSTANCE: NoorDatabase? = null

        fun getInstance(context: Context): NoorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoorDatabase::class.java,
                    "noor_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
