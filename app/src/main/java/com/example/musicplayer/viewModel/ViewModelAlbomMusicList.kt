package com.example.musicplayer.viewModel

import androidx.lifecycle.LiveData
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.model.EventWithBlock
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge

interface ViewModelAlbomMusicList {
    val openAddMusicLiveData: LiveData<Event<Long>>
    val openMusicPlayerLiveData: LiveData<Event<Unit>>
    val loadMusicsLiveData: LiveData<Event<List<MusicDataStoradge>>>
    val loadAlbomNameLiveData: LiveData<Event<String>>
    val backLiveData: LiveData<Event<Unit>>
    val deleteLiveData: LiveData<EventWithBlock<MusicDataStoradge, MusicDataStoradge,Unit>>
    val showMassageLiveData: LiveData<Event<String>>

    fun back()
    fun loadData()
    fun openMusicPlayer(id: Long)
    fun delete(data: MusicDataStoradge, albomId:Long)
    fun openAddMusicPlayer(albomId:Long)
}