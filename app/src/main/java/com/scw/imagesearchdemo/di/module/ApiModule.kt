package com.scw.imagesearchdemo.di.module

import com.scw.imagesearchdemo.network.ImageApiService
import org.koin.dsl.module

val apiModule = module {
    single { ImageApiService() }
    single { get<ImageApiService>().pixabayApi() }
}