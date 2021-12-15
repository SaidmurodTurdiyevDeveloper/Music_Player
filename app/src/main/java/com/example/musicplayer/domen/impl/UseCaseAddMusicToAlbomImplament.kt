package com.example.musicplayer.domen.impl

import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.model.HelperData
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import com.example.musicplayer.domen.UseCaseAddMusicToAlbom
import com.example.musicplayer.domen.repostory.RepostoryMusicAddToAlbom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UseCaseAddMusicToAlbomImplament @Inject constructor(private var repostory: RepostoryMusicAddToAlbom) :
    UseCaseAddMusicToAlbom {
    override fun loadList(): Flow<List<HelperData<Boolean, MusicDataStoradge>>> = flow {
        val list = ArrayList<HelperData<Boolean, MusicDataStoradge>>()
        ConstantMusic.getList().forEach {
            list.add(HelperData(false, it))
        }
        emit(list)
    }

    override suspend fun saveList(list: List<EntityAlbomMusics>?): Flow<Boolean> = flow {
        if (list != null && list.isNotEmpty()) {
            repostory.setList(list)
            emit(true)
        } else {
            emit(false)
        }
    }
}