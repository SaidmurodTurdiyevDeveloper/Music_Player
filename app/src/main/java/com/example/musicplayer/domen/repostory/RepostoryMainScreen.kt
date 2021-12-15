package com.example.musicplayer.domen.repostory

import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics


interface RepostoryMainScreen {

    suspend fun addFavaurute(data:EntityFavouruteMusics): Long

    suspend fun deleteFavaurute(data: EntityFavouruteMusics): Int

    suspend fun deleteFavaurute(musicId:Long)
}