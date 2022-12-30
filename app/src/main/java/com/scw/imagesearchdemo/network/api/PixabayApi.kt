package com.scw.imagesearchdemo.network.api

import com.scw.imagesearchdemo.model.entity.ImageEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("api/?key=32459772-0cc6c885dd9f2082182070903&image_type=photo")
    fun fetch(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Flow<ImageEntity>
}