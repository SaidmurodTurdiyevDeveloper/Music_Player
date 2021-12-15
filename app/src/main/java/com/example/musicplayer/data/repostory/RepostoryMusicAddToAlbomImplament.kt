package com.example.musicplayer.data.repostory

import com.example.musicplayer.data.source.local.room.DatabaseMusic
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import com.example.musicplayer.domen.repostory.RepostoryMusicAddToAlbom
import javax.inject.Inject

class RepostoryMusicAddToAlbomImplament @Inject constructor(databaseMusic: DatabaseMusic) : RepostoryMusicAddToAlbom {
    private var dao = databaseMusic.getDaoAddMusicToAlboms()

    override suspend fun setList(list: List<EntityAlbomMusics>) {
        dao.setMusicToAlbom(list)
    }
}