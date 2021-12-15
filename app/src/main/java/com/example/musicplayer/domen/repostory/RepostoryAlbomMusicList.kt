package com.example.musicplayer.domen.repostory

import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import kotlinx.coroutines.flow.Flow

interface RepostoryAlbomMusicList {
    suspend fun getAlbom(id: Long): List<EntityAlbomMusics>
    suspend fun deleteMusic(data: EntityAlbomMusics): Int
    suspend fun deleteMusicWithId(albomId: Long, musicid: Long)
}