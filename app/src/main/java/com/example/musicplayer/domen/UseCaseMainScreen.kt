package com.example.musicplayer.domen

import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import kotlinx.coroutines.flow.Flow

interface UseCaseMainScreen {
    fun changeFavaurute(): Flow<MusicDataStoradge>
}