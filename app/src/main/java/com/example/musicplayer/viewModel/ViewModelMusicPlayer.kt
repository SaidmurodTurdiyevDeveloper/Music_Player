package com.example.musicplayer.viewModel

import androidx.lifecycle.LiveData
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge

interface ViewModelMusicPlayer {
    val nextMuiscLiveData: LiveData<Event<Unit>>
    val previousMuiscLiveData: LiveData<Event<Unit>>
    val aboutInfoMuiscLiveData: LiveData<Event<MusicDataStoradge>>
    val repeatMuiscLiveData: LiveData<Event<Boolean>>
    val shuffleMuiscLiveData: LiveData<Event<Boolean>>
    val openListLivedata: LiveData<Event<Unit>>
    val setLikeMusicLivdata: LiveData<Event<Boolean>>
    val showMassageLiveData: LiveData<Event<String>>

    fun playPaus()
    fun next()
    fun previous()
    fun shuffle()
    fun repeat()
    fun info()
    fun like()
    fun openList()
}