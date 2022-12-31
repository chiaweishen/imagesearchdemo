package com.scw.imagesearchdemo.di.module

import com.scw.imagesearchdemo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
}