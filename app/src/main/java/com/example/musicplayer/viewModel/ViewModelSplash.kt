package com.example.musicplayer.viewModel

import androidx.lifecycle.LiveData
import com.example.musicplayer.data.model.Event

interface ViewModelSplash {
    val openMainScreenLiveData:LiveData<Event<Unit>>
    val showMassageLiveData:LiveData<Event<String>>
    fun loadMusics()
}