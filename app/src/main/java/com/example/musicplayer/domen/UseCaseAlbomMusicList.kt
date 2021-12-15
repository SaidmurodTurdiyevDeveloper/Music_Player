package com.example.musicplayer.domen

import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import kotlinx.coroutines.flow.Flow

interface UseCaseAlbomMusicList {
    suspend fun loadList(id: Long, action: String): Flow<List<MusicDataStoradge>>
    suspend fun delete(albomId: Long, musicId: Long): Flow<Boolean>
}