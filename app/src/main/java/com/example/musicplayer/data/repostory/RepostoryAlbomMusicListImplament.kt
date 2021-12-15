package com.example.musicplayer.data.repostory

import com.example.musicplayer.data.source.local.room.DatabaseMusic
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics
import com.example.musicplayer.domen.repostory.RepostoryAlbomMusicList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepostoryAlbomMusicListImplament @Inject constructor(database: DatabaseMusic) :
    RepostoryAlbomMusicList {

    private var dao = database.getDaoAlbomMusics()

    override suspend fun deleteMusic(data: EntityAlbomMusics): Int = dao.deleteMusicFromAlbom(data)

    override suspend fun getAlbom(id: Long): List<EntityAlbomMusics> = dao.getAlbomMusicList(id)

    override suspend fun deleteMusicWithId(albomId: Long, musicid: Long) {
        dao.deleteMusicWithAlbomIdAndMusicId(albomId, musicid)
    }
}