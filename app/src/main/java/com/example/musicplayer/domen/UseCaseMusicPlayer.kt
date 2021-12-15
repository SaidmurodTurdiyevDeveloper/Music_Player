package com.example.musicplayer.domen

import kotlinx.coroutines.flow.Flow

interface UseCaseMusicPlayer {
    suspend fun getShuffle():Flow<Boolean>
    suspend fun setShuffle()
    suspend fun getRepeat():Flow<Boolean>
    suspend fun setRepeat()
    suspend fun favourute():Flow<Boolean>
}