package com.example.musicplayer.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge

object ConstantMusic {
    private val allMusicList = ArrayList<MusicDataStoradge>()
    private val currentMusicList = ArrayList<MusicDataStoradge>()
    private var currentMusic = MusicDataStoradge()

    private val _loadCurrentMusicLiveData = MutableLiveData<Event<MusicDataStoradge>>()
    val loadCurrentMusicLiveData: LiveData<Event<MusicDataStoradge>> = _loadCurrentMusicLiveData

    val loadMusicDurationLiveData = MutableLiveData<Event<Int>>()
    val playPauseMusicLiveData = MutableLiveData<Event<Boolean>>()
    val changePlayPauseMusicLiveData = MutableLiveData<Event<Boolean>>()

    fun loadAllMusicList(list: List<MusicDataStoradge>) {
        if (list.isNotEmpty()) {
            allMusicList.clear()
            currentMusicList.clear()
            currentMusicList.addAll(list)
            allMusicList.addAll(list)
        }
    }

    fun setCurrentList(list: List<MusicDataStoradge>) {
        if (list.isNotEmpty()) {
            currentMusicList.clear()
            currentMusicList.addAll(list)
        }
    }

    fun getCurrentMusic() = currentMusic

    fun getCurrentList(): List<MusicDataStoradge> = currentMusicList

    fun setCurrentData(id: Long) {
        getCurrentIdMusic(id).also {
            _loadCurrentMusicLiveData.value = Event(it)
            currentMusic = it
        }
    }

    fun getList(): List<MusicDataStoradge> = allMusicList

    fun getCurrentIdMusic(id: Long): MusicDataStoradge = with(allMusicList) {
        if (id == -1L)
            return@with MusicDataStoradge()
        forEach {
            if (it.id == id)
                return@with it
        }
        MusicDataStoradge()
    }

    fun getFavauruteList(): List<MusicDataStoradge> = allMusicList.filter {
            it.favourute
        }
}