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

    private val _imageLiveData = MutableLiveData<PagingData<Image>>()
    val imageLiveData: LiveData<PagingData<Image>> = _imageLiveData

    fun search(query: String) {
        viewModelScope.launch {
            recentRepository.addRecentQuery(query)
            imageRepository.fetchImages(query).cachedIn(viewModelScope).collectLatest {
                _imageLiveData.value = it
            }
        }
    }

    fun loadRecentData() {
        viewModelScope.launch {
            recentRepository.loadRecentData().collectLatest {
                _recentLiveData.value = it
            }
        }
    }
}