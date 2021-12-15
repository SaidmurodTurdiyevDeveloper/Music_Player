package com.example.musicplayer.data.repostory

import com.example.musicplayer.data.source.local.room.DatabaseMusic
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import com.example.musicplayer.domen.repostory.RepostoryAlbom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepostoryAlbomImplament @Inject constructor(databaseMusic: DatabaseMusic) :
    RepostoryAlbom {
    private var albomdao = databaseMusic.getDaoAlboms()

    override suspend fun getAlbomList(): Flow<List<EntityAlboms>> = albomdao.getalbomList()

    override suspend fun addAlbom(data: EntityAlboms) {
        albomdao.insertAlbom(data)
    }

    override suspend fun updateAlbom(data: EntityAlboms) {
        albomdao.updateAlbom(data)
    }

    override suspend fun deleteAlbom(data: EntityAlboms) {
        albomdao.delete(data)
    }

}