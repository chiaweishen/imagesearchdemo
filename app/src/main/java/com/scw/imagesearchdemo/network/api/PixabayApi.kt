package com.scw.imagesearchdemo.network.api

import com.scw.imagesearchdemo.model.entity.ImageInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("api/?image_type=photo&per_page=50")
    fun fetch(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): Flow<ImageInfo>
}