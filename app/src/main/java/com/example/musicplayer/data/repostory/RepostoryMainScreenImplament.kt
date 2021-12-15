package com.example.musicplayer.data.repostory

import com.example.musicplayer.data.source.local.room.DatabaseMusic
import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics
import com.example.musicplayer.domen.repostory.RepostoryMainScreen
import javax.inject.Inject

class RepostoryMainScreenImplament @Inject constructor(
    databaseMusic: DatabaseMusic
) : RepostoryMainScreen {
    private var dao = databaseMusic.getFavoruteMusicDao()

    override suspend fun addFavaurute(data: EntityFavouruteMusics): Long = dao.insert(data)

    override suspend fun deleteFavaurute(data: EntityFavouruteMusics): Int = dao.delete(data)

    override suspend fun deleteFavaurute(musicId: Long) = dao.deleteWithAlbomId(musicId)
}
