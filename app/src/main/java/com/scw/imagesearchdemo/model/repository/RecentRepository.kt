package com.scw.imagesearchdemo.model.repository

import com.scw.imagesearchdemo.db.dao.RecentDao
import com.scw.imagesearchdemo.db.entity.RecentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface RecentRepository {
    suspend fun addRecentQuery(query: String)
    fun getRecentData(): List<String>
    fun loadRecentData(): Flow<List<String>>
}

class RecentRepositoryImpl(
    private val recentDao: RecentDao
): RecentRepository {

    companion object {
        private const val RECENT_LIMIT = 20
    }

    private val recentData = arrayListOf<String>()

    override suspend fun addRecentQuery(query: String) {
        recentData.add(0, query)
        recentDao.insert(RecentEntity(query = query))
    }

    override fun getRecentData(): List<String> {
        return recentData.subList(0, RECENT_LIMIT.coerceAtMost(recentData.size))
    }

    override fun loadRecentData(): Flow<List<String>> {
        return recentDao.query(RECENT_LIMIT).map {
            recentData.clear()
            it.forEach { entity ->
                recentData.add(entity.query)
            }
            recentData
        }
    }
}
