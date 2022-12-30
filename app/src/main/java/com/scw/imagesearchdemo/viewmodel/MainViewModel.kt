package com.scw.imagesearchdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scw.imagesearchdemo.model.data.Image
import com.scw.imagesearchdemo.model.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(
    private val repository: ImageRepository
) : ViewModel() {
    fun loadImages(query: String): Flow<PagingData<Image>> {
        return repository.fetchImages(query).cachedIn(viewModelScope)
    }
}