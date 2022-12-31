package com.scw.imagesearchdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scw.imagesearchdemo.db.dao.RecentDao
import com.scw.imagesearchdemo.db.entity.RecentEntity

@Database(entities = [RecentEntity::class], version = 1)
abstract class SearchDatabase : RoomDatabase() {

    abstract fun recentDao(): RecentDao

    companion object {
        private var INSTANCE: SearchDatabase? = null

        fun getInstance(context: Context): SearchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SearchDatabase::class.java,
                    "search_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}