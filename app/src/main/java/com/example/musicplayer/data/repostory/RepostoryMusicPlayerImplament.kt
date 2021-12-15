package com.example.musicplayer.data.repostory

import com.example.musicplayer.data.source.local.room.DatabaseMusic
import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics
import com.example.musicplayer.data.source.local.shared.LocalStoradge
import com.example.musicplayer.domen.repostory.RepostoryMusicPlayer
import com.example.musicplayer.zZz__utilites.MyAppConstants
import javax.inject.Inject

class RepostoryMusicPlayerImplament @Inject constructor(databaseMusic: DatabaseMusic, storadge: LocalStoradge) : RepostoryMusicPlayer {
    private var localStoradge = storadge
    private var dao = databaseMusic.getFavoruteMusicDao()

    override suspend fun setRepeat(data: Int) = localStoradge.saveInt(data, MyAppConstants.KEY_REPEAT_CONDITION)

    override suspend fun getRepeat(): Int = localStoradge.getInt(MyAppConstants.KEY_REPEAT_CONDITION)

    override suspend fun setShuffle(data: Int) = localStoradge.saveInt(data, MyAppConstants.KEY_SHUFFLE_CONDITION)

    override suspend fun getShuffle(): Int = localStoradge.getInt(MyAppConstants.KEY_SHUFFLE_CONDITION)

    override suspend fun addFavaurute(data: EntityFavouruteMusics): Long = dao.insert(data)

    override suspend fun deleteFavaurute(data: EntityFavouruteMusics): Int = dao.delete(data)

    override suspend fun deleteFavaurute(musicId: Long) = dao.deleteWithAlbomId(musicId)
}