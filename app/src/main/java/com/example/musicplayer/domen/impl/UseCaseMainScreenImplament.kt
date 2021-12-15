package com.example.musicplayer.domen.impl

import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics
import com.example.musicplayer.domen.UseCaseMainScreen
import com.example.musicplayer.domen.repostory.RepostoryMainScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UseCaseMainScreenImplament @Inject constructor(private var repository: RepostoryMainScreen) : UseCaseMainScreen {

    override fun changeFavaurute(): Flow<MusicDataStoradge> = flow {
        val data = ConstantMusic.getCurrentMusic()
        if (data.favourute) {
            data.favourute = false
            repository.deleteFavaurute(data.id ?: -1)
        } else {
            data.favourute = true
            repository.addFavaurute(EntityFavouruteMusics(data.id ?: -1))
        }
        emit(data)
    }
}