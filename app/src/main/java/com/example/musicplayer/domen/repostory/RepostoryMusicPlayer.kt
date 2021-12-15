package com.example.musicplayer.domen.repostory

import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics

interface RepostoryMusicPlayer {
    suspend fun setRepeat(data:Int)
    suspend fun getRepeat():Int
    suspend fun setShuffle(data: Int)
    suspend fun getShuffle():Int
    suspend fun addFavaurute(data:EntityFavouruteMusics): Long
    suspend fun deleteFavaurute(data: EntityFavouruteMusics): Int
    suspend fun deleteFavaurute(musicId:Long)
}