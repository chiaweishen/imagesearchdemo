package com.scw.imagesearchdemo.di.module

import com.scw.imagesearchdemo.db.SearchDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single { SearchDatabase.getInstance(androidContext()) }
    single { get<SearchDatabase>().recentDao() }
}