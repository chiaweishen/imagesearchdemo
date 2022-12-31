package com.scw.imagesearchdemo.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_table")
data class RecentEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var query: String
)
