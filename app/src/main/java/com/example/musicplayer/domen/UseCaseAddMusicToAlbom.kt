package com.example.musicplayer.domen

import com.example.musicplayer.data.model.HelperData
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import kotlinx.coroutines.flow.Flow

interface UseCaseAddMusicToAlbom {
    fun loadList():Flow<List<HelperData<Boolean, MusicDataStoradge>>>
    suspend fun saveList(list: List<EntityAlbomMusics>?):Flow<Boolean>
}