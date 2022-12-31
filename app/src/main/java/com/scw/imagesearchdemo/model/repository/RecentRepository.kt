package com.scw.imagesearchdemo.model.repository

import com.scw.imagesearchdemo.db.dao.RecentDao
import com.scw.imagesearchdemo.db.entity.RecentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface RecentRepository {
    suspend fun addRecentQuery(query: String)
    fun loadRecentData(): Flow<List<String>>
}

class RecentRepositoryImpl(
    private val recentDao: RecentDao
): RecentRepository {

    companion object {
        private const val RECENT_LIMIT = 10
    }

    override suspend fun addRecentQuery(query: String) {
        recentDao.insert(RecentEntity(query = query))
    }

    override fun loadRecentData(): Flow<List<String>> {
        return recentDao.query(RECENT_LIMIT).map {
            val recentData = arrayListOf<String>()
            it.forEach { entity ->
                recentData.add(entity.query)
            }
            recentData
        }
    }
}
