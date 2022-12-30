package com.scw.imagesearchdemo

import android.app.Application
import android.content.pm.PackageManager
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.scw.imagesearchdemo.di.module.apiModule
import com.scw.imagesearchdemo.util.MyDebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication : Application(), ImageLoaderFactory {

    companion object {
        private lateinit var INSTANCE: MyApplication
        private lateinit var API_KEY: String

        fun get() = INSTANCE
        fun getApiKey() = API_KEY
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initTimber()
        initApiKey()
        initKoinModules()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(MyDebugTree())
        }
    }

    private fun initApiKey() {
        applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        ).apply {
            API_KEY = metaData["api_key"].toString()
            Timber.i("Api Key: $API_KEY")
        }
    }

    private fun initKoinModules() {
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(apiModule))
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .crossfade(250)
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