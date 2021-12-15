package com.example.musicplayer.domen.impl

import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import com.example.musicplayer.domen.UseCaseAlbomMusicList
import com.example.musicplayer.domen.repostory.RepostoryAlbomMusicList
import com.example.musicplayer.zZz__utilites.MyAppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class UseCaseAlbomMusicListImplament @Inject constructor(private var repostory: RepostoryAlbomMusicList) : UseCaseAlbomMusicList {
    override suspend fun loadList(id: Long, action: String): Flow<List<MusicDataStoradge>> = flow {
        Timber.d("Ishlayabdi id="+id.toString()+" action -> "+action)
        when (action) {
            MyAppConstants.KEY_ALL_MEDIOPLAYER -> {
                emit(ConstantMusic.getList().shuffled())
            }
            MyAppConstants.KEY_FAVORUTE_MEDIOPLAYER -> {
                emit(ConstantMusic.getFavauruteList())
            }
            MyAppConstants.KEY_MOVE_ALBOM_ID_MEDIOPLAYER -> {
                Timber.d("Keldi id bilan")
                val list = repostory.getAlbom(id)
                Timber.d(list.size.toString())
                val newList = ArrayList<MusicDataStoradge>()
                list.forEach { musicData ->
                    val d = ConstantMusic.getCurrentIdMusic(musicData.musicId)
                    if (d.id != null) {
                        Timber.d("Qo`shildi")
                        newList.add(d)
                    } else {
                        Timber.d("O`chirildi")
                        repostory.deleteMusic(musicData)
                    }
                }
                emit(newList)
            }
        }
    }

    override suspend fun delete(albomId: Long, musicId: Long): Flow<Boolean> = flow {
        repostory.deleteMusicWithId(albomId, musicId)
        emit(true)
    }


}