package com.example.musicplayer.domen.repostory

import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics
import kotlinx.coroutines.flow.Flow

interface RepostorySplash {
    suspend fun init(): Flow<ArrayList<MusicDataStoradge>>
    suspend fun getFavorute(): List<EntityFavouruteMusics>
    suspend fun getShuffle(): Int
    suspend fun getRepeat(): Int
    suspend fun relaizeAlbomMuzics(): List<EntityAlbomMusics>
    suspend fun getCurrentMusicId():Long
    suspend fun deletEmptyAlbomIdMusics(list: List<EntityAlbomMusics>):Int
    suspend fun deletEmptyFavouruteMusics(list: List<EntityFavouruteMusics>)
}