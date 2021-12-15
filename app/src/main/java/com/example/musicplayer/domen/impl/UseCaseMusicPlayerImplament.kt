package com.example.musicplayer.domen.impl

import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics
import com.example.musicplayer.domen.UseCaseMusicPlayer
import com.example.musicplayer.domen.repostory.RepostoryMusicPlayer
import com.example.musicplayer.service.ServiceHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UseCaseMusicPlayerImplament @Inject constructor(private var repostory: RepostoryMusicPlayer) :
    UseCaseMusicPlayer {
    override suspend fun getShuffle(): Flow<Boolean> = flow {
        val cond = repostory.getShuffle() == 1
        ServiceHelper.changeShuffle(cond)
        emit(cond)
    }

    override suspend fun setShuffle() {
        with(repostory) {
            setShuffle(
                if (getShuffle() == 1) {
                    0
                } else {
                    1
                }
            )
        }
    }

    override suspend fun getRepeat(): Flow<Boolean> = flow {
        val cond = repostory.getRepeat() == 1
        ServiceHelper.changeRepeat(cond)
        emit(cond)
    }

    override suspend fun setRepeat() {
        with(repostory) {
            setRepeat(
                if (getRepeat() == 1) {
                    0
                } else {
                    1
                }
            )
        }
    }

    override suspend fun favourute(): Flow<Boolean> = flow {
        val data = ConstantMusic.getCurrentMusic()
        if (data.favourute) {
            data.favourute = false
            repostory.deleteFavaurute(data.id ?: -1)
        } else {
            data.favourute = true
            repostory.addFavaurute(EntityFavouruteMusics(data.id ?: -1))
        }
        emit(data.favourute)
    }
}