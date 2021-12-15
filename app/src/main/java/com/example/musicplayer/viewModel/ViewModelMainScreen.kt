package com.example.musicplayer.viewModel

import androidx.lifecycle.LiveData
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge

interface ViewModelMainScreen {
    val openAlbomLivedata: LiveData<Event<Unit>>
    val setNextLivedata: LiveData<Event<Unit>>
    val setPreviousLivedata: LiveData<Event<Unit>>
    val setLikeMusicLivedata: LiveData<Event<Boolean>>
    val openMusicPlayerLiveData: LiveData<Event<Unit>>
    val showMassageLiveData: LiveData<Event<String>>
    val setCurrentMusicToService: LiveData<Event<Unit>>
    val loadAllMusicLivedata: LiveData<Event<List<MusicDataStoradge>>>

    fun openAlbom()
    fun fovorute()
    fun playPaus()
    fun next()
    fun previous()
    fun itemClick(data: MusicDataStoradge)
    fun openPlayer()
}