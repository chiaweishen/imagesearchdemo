package com.scw.imagesearchdemo.model.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scw.imagesearchdemo.model.data.Image
import com.scw.imagesearchdemo.network.api.PixabayApi
import kotlinx.coroutines.flow.first
import timber.log.Timber

class ImagePagingSource(
    private val pixabayApi: PixabayApi,
    private val query: String,
    private val perPageSize: Int
) : PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let { position ->
            val preKey = state.closestPageToPosition(position)?.prevKey
            val nextKey = state.closestPageToPosition(position)?.nextKey
            Timber.i("getRefreshKey preKey: $preKey, nextKey: $nextKey")
            preKey?.plus(1) ?: nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        return try {
            val list = mutableListOf<Image>()
            val position = params.key ?: 1

            val imageEntity = pixabayApi.fetch(query, position, perPageSize).first()
            imageEntity.hits.forEach {
                list.add(Image(it.id, it.webformatURL))
            }

            val preKey = if (position == 1) null else position - 1
            val nextKey = if (list.size < perPageSize) null else position + 1
            Timber.i("load position: $position, preKey: $preKey, nextKey: $nextKey")
            LoadResult.Page(list, preKey, nextKey)
        } catch (e: Exception) {
            Timber.e("load error: ${e.message}")
            LoadResult.Error(e)
        }
    }
}