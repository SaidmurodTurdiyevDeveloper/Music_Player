package com.example.musicplayer.domen.impl

import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import com.example.musicplayer.domen.UseCaseAlbom
import com.example.musicplayer.domen.repostory.RepostoryAlbom
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UseCaseAlbomImplament @Inject constructor(private var repostory: RepostoryAlbom) :
    UseCaseAlbom {

    override suspend fun getAlbom(): Flow<List<EntityAlboms>> = repostory.getAlbomList()

    override suspend fun add(data: EntityAlboms) {
        repostory.addAlbom(data)
    }

    override suspend fun update(data: EntityAlboms) {
        repostory.updateAlbom(data)
    }

    override suspend fun delete(data: EntityAlboms) {
        repostory.deleteAlbom(data)
    }
}