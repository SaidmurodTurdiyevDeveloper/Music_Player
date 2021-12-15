package com.example.musicplayer.viewModel

import androidx.lifecycle.LiveData
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.model.HelperData
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics

interface ViewModelAddMusicToAlbom {
    val backLiveData: LiveData<Event<Unit>>
    val loadMusicLiveData: LiveData<Event<List<HelperData<Boolean, MusicDataStoradge>>>>

    fun done(list:List<EntityAlbomMusics>)
    val showMassageLiveData: LiveData<Event<String>>
    fun close()

}