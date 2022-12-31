package com.scw.imagesearchdemo

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.scw.imagesearchdemo.di.module.apiModule
import com.scw.imagesearchdemo.di.module.dbModule
import com.scw.imagesearchdemo.di.module.repositoryModule
import com.scw.imagesearchdemo.di.module.viewModelModule
import com.scw.imagesearchdemo.util.MyDebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication : Application(), ImageLoaderFactory {

    companion object {
        private lateinit var INSTANCE: MyApplication
        fun get() = INSTANCE
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initTimber()
        initKoinModules()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(MyDebugTree())
        }
    }

    private fun initKoinModules() {
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(apiModule, repositoryModule, viewModelModule, dbModule))
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(false)
            .placeholder(R.drawable.ic_baseline_downloading)
            .error(R.drawable.ic_baseline_error_outline)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.1)
                    .build()
            }
            .build()
    }
}