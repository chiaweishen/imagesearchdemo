package com.scw.imagesearchdemo.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.scw.imagesearchdemo.model.data.Image
import com.scw.imagesearchdemo.model.datasource.ImagePagingSource
import com.scw.imagesearchdemo.network.api.PixabayApi
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun fetchImages(query: String): Flow<PagingData<Image>>
}

class ImageRepositoryImpl(private val pixabayApi: PixabayApi) : ImageRepository {

    companion object {
        private const val PAGE_SIZE = 50;
    }

    override fun fetchImages(query: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ImagePagingSource(pixabayApi, query, PAGE_SIZE)
            },
            initialKey = 1
        ).flow
    }

}