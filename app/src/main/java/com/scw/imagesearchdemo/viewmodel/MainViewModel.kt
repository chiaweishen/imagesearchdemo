package com.scw.imagesearchdemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scw.imagesearchdemo.model.data.Image
import com.scw.imagesearchdemo.model.repository.ImageRepository
import com.scw.imagesearchdemo.model.repository.RecentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val imageRepository: ImageRepository,
    private val recentRepository: RecentRepository
) : ViewModel() {

    private val _recentLiveData = MutableLiveData<List<String>>()
    val recentLiveData: LiveData<List<String>> = _recentLiveData

    fun search(query: String): Flow<PagingData<Image>> {
        viewModelScope.launch {
            recentRepository.addRecentQuery(query)
        }
        return imageRepository.fetchImages(query).cachedIn(viewModelScope)
    }

    fun loadRecentData() {
        viewModelScope.launch {
            recentRepository.loadRecentData().collectLatest {
                _recentLiveData.value = it
            }
        }
    }
}