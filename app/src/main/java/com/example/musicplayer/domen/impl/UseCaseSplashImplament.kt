package com.example.musicplayer.domen.impl

import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.model.ResponseData
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics
import com.example.musicplayer.domen.UseCaseSplash
import com.example.musicplayer.domen.repostory.RepostorySplash
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class UseCaseSplashImplament @Inject constructor(private var repostory: RepostorySplash) :
    UseCaseSplash {
    override suspend fun init(): Flow<ResponseData> = flow {
        try {
            emit(ResponseData.Loading())
            val newFavouruteMusicList = ArrayList<EntityFavouruteMusics>()
            repostory.init().collect { musicList ->
                val favouruteList = repostory.getFavorute()
                newFavouruteMusicList.clear()
                newFavouruteMusicList.addAll(favouruteList)
                var data: MusicDataStoradge? = null
                val currentId = repostory.getCurrentMusicId()
            Timber.d("1")
                for (item in musicList) {
                    if (currentId == item.id)
                        data = item
                    initFavourute(newFavouruteMusicList, item)?.let {
                        newFavouruteMusicList.remove(it)
                    }
                }
                if (favouruteList.isNotEmpty())
                    repostory.deletEmptyFavouruteMusics(newFavouruteMusicList)
                val realizeList = repostory.relaizeAlbomMuzics()
                if (realizeList.isNotEmpty())
                    repostory.deletEmptyAlbomIdMusics(realizeList)
                ConstantMusic.loadAllMusicList(musicList)
                if (data != null) {
                    ConstantMusic.setCurrentData(data.id ?: -1)
                }
                emit(ResponseData.Open())
            }
        } catch (e: Exception) {
            emit(ResponseData.Error(e.message.toString()))
        }
    }

    private fun initFavourute(
        list: List<EntityFavouruteMusics>,
        data: MusicDataStoradge
    ): EntityFavouruteMusics? {
        if (list.isEmpty())
            return EntityFavouruteMusics(-1)
        for (item in list) {
            if (item.musicId == data.id) {
                data.favourute = true
                return item
            }
        }
        return null
    }
}