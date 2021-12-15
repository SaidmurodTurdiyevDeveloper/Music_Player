package com.example.musicplayer.data.repostory

import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.DatabaseMusic
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics
import com.example.musicplayer.data.source.local.shared.LocalStoradge
import com.example.musicplayer.domen.repostory.RepostorySplash
import com.example.musicplayer.data.source.local.mediaStoradge.MusicList
import com.example.musicplayer.zZz__utilites.MyAppConstants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepostorySplashImplament @Inject constructor(database: DatabaseMusic,
                                                   private var storadge: LocalStoradge,
                                                   private var musiclist: MusicList) : RepostorySplash {

    private var daoFavourute = database.getFavoruteMusicDao()

    private var daoAlbom = database.getDaoAlbomMusics()

    override suspend fun init(): Flow<ArrayList<MusicDataStoradge>> = musiclist.init()

    override suspend fun getFavorute(): List<EntityFavouruteMusics> = daoFavourute.getFavouruteMusicList()

    override suspend fun getShuffle():Int = storadge.getInt(MyAppConstants.KEY_SHUFFLE_CONDITION)

    override suspend fun getRepeat(): Int = storadge.getInt(MyAppConstants.KEY_REPEAT_CONDITION)

    override suspend fun getCurrentMusicId(): Long = storadge.getLong(MyAppConstants.KEY_CURRENT_ITEM)

    override suspend fun relaizeAlbomMuzics(): List<EntityAlbomMusics> = daoAlbom.releazeAlbomMusics()

    override suspend fun deletEmptyAlbomIdMusics(list: List<EntityAlbomMusics>):Int = daoAlbom.deleteMusicListFromAlbom(list)

    override suspend fun deletEmptyFavouruteMusics(list: List<EntityFavouruteMusics>) = daoFavourute.deleteMusicListFromAlbom(list)
}