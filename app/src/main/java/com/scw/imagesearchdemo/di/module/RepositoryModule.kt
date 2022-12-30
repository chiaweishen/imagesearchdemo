package com.scw.imagesearchdemo.di.module

import com.scw.imagesearchdemo.model.repository.ImageRepository
import com.scw.imagesearchdemo.model.repository.ImageRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ImageRepository> { ImageRepositoryImpl(get()) }
}