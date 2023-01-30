package com.isidroid.b21.data.source.local

import android.content.Context
import androidx.room.*
import com.isidroid.b21.data.source.local.dao.PostDao
import com.isidroid.b21.data.source.local.dao.SessionDao
import com.isidroid.b21.domain.model.Post
import com.isidroid.b21.domain.model.Session

@Database(
    entities = [Session::class, Post::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
    ]
)

@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract val postDao: PostDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "database")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}