package com.example.musicplayer.domen

import com.example.musicplayer.data.model.ResponseData
import kotlinx.coroutines.flow.Flow


interface UseCaseSplash {
    suspend fun init(): Flow<ResponseData>
}