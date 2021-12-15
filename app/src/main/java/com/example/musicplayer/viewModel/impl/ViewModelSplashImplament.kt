package com.example.musicplayer.viewModel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.model.ResponseData
import com.example.musicplayer.domen.UseCaseSplash
import com.example.musicplayer.viewModel.ViewModelSplash
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelSplashImplament @Inject constructor(private var useCaseSplash: UseCaseSplash) : ViewModelSplash, ViewModel() {

    private val _openMainScreenLiveData = MutableLiveData<Event<Unit>>()
    override val openMainScreenLiveData: LiveData<Event<Unit>> get() = _openMainScreenLiveData

    private val _showMassageLiveData = MutableLiveData<Event<String>>()
    override val showMassageLiveData: LiveData<Event<String>> get() = _showMassageLiveData

    override fun loadMusics() {
        viewModelScope.launch {
            useCaseSplash.init().onEach {
                when (it) {
                    is ResponseData.Loading -> {
                        _showMassageLiveData.postValue(Event("Loading data"))
                    }
                    is ResponseData.Error -> {
                        _showMassageLiveData.postValue(Event(it.message))
                    }
                    is ResponseData.Open -> {
                        _openMainScreenLiveData.postValue(Event(Unit))
                    }
                }
            }.catch {
                _showMassageLiveData.postValue(Event("Musiqalarni o`qishda xatolik yuz beri!"))
            }.launchIn(viewModelScope)
        }
    }
}