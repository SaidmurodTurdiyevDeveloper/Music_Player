package com.example.musicplayer.domen

import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import kotlinx.coroutines.flow.Flow

interface UseCaseAlbom {
    suspend fun getAlbom():Flow<List<EntityAlboms>>
    suspend fun add(data:EntityAlboms)
    suspend fun update(data:EntityAlboms)
    suspend fun delete(data:EntityAlboms)
}