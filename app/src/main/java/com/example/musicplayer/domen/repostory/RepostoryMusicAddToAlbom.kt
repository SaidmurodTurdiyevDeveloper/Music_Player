package com.example.musicplayer.domen.repostory

import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics

interface RepostoryMusicAddToAlbom {
    suspend fun setList(list:List<EntityAlbomMusics>)
}