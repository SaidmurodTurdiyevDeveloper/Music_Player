package com.example.musicplayer.viewModel

import androidx.lifecycle.LiveData
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.model.EventWithBlock
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms

interface ViewModelAlbomScreen {
        val openShuffleLiveData:LiveData<Event<Unit>>
        val openFavoruteLiveData:LiveData<Event<Unit>>
        val openAlbomLiveData:LiveData<Event<Long>>
        val backLiveData:LiveData<Event<Unit>>
        val loadAlbomItemLiveData:LiveData<Event<List<EntityAlboms>>>
        val updateAlbomItemLiveData:LiveData<EventWithBlock<EntityAlboms,EntityAlboms,Unit>>
        val deleteAlbomItemLiveData:LiveData<EventWithBlock<EntityAlboms, EntityAlboms, Unit>>
        val createAlbomItemLiveData:LiveData<EventWithBlock<EntityAlboms, EntityAlboms, Unit>>
        val showMassageLiveData:LiveData<Event<String>>

        fun back()
        fun shuffle()
        fun favorute()
        fun itemClick(data: EntityAlboms)
        fun edit(data: EntityAlboms)
        fun delete(data: EntityAlboms)
        fun createAlbom()
}