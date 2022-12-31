package com.scw.imagesearchdemo.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scw.imagesearchdemo.db.entity.RecentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentDao {
    @Query("SELECT * FROM recent_table ORDER BY id DESC LIMIT :limit")
    fun query(limit: Int? = -1): Flow<List<RecentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recent: RecentEntity)
}