package com.example.musicplayer.domen.repostory

import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import kotlinx.coroutines.flow.Flow

interface RepostoryAlbom {

    suspend fun getAlbomList(): Flow<List<EntityAlboms>>

    suspend fun addAlbom(data: EntityAlboms)

    suspend fun updateAlbom(data: EntityAlboms)

    suspend fun deleteAlbom(data: EntityAlboms)

}