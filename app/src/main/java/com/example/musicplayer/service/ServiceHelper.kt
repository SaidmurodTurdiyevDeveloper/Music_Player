package com.example.musicplayer.service

import android.content.Context
import android.media.MediaPlayer
import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ServiceHelper @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private var mediaPlayer: MediaPlayer? = null
        private var shuffle = false
        private var repeatOne = false

        var isActiveMusicPlayer = false

        fun getMusicPlayer(): MediaPlayer? = mediaPlayer

        fun changeShuffle(cond: Boolean) {
            shuffle = cond
        }

        fun changeRepeat(cond: Boolean) {
            repeatOne = cond
        }
    }

    private var mycontext = context
    private var currentMusicPosition = -1

    fun nextMusic(block: (MusicDataStoradge) -> Unit) {
        val data = if (shuffle){
            currentMusicPosition = (ConstantMusic.getCurrentList().indices).random()
            ConstantMusic.getCurrentList()[currentMusicPosition]
        } else getNextMusic()
        ConstantMusic.setCurrentData(data.id ?: -1)
        loadMusicToMusicPlayer(data, block)

    }

    fun prevousMusic(block: (MusicDataStoradge) -> Unit) {
        val data = if (shuffle) {
            currentMusicPosition = (ConstantMusic.getCurrentList().indices).random()
            ConstantMusic.getCurrentList()[currentMusicPosition]
        } else getPrevoiusMusic()
        ConstantMusic.setCurrentData(data.id ?: -1)
        loadMusicToMusicPlayer(data, block)
    }

    fun start(block: (MusicDataStoradge) -> Unit) {
        val data = ConstantMusic.getCurrentMusic()
        if (data.id != null) {
            if (mediaPlayer == null) {
                medioPlayerCreatePlay(data, block)
            } else {
                mediaPlayer?.let {
                    if (!it.isPlaying)
                        mediaPlayer?.start()
                }
            }
            isActiveMusicPlayer = true
            ConstantMusic.changePlayPauseMusicLiveData.postValue(Event(true))
            block.invoke(data)
            startSeekBar()
        }
    }

    fun paus(block: () -> Unit) {
        if (isActiveMusicPlayer) {
            mediaPlayer?.let { mdp ->
                mdp.pause()
                isActiveMusicPlayer = false
                ConstantMusic.changePlayPauseMusicLiveData.postValue(Event(false))
                block.invoke()
            }
        }
    }

    fun setDuration(duration: Int) {
        mediaPlayer?.let { mdp ->
            if (!mdp.isPlaying) {
                mdp.start()
                ConstantMusic.playPauseMusicLiveData.postValue(Event(true))
            }
            mdp.seekTo(duration)
            startSeekBar()
        }
    }

    fun stopMediaplayer() {
        mediaPlayer?.let { mdp ->
            mdp.stop()
            mdp.release()
            mediaPlayer = null
        }
    }

    private fun loadMusicToMusicPlayer(data: MusicDataStoradge, block: (MusicDataStoradge) -> Unit) {
        stopMediaplayer()
        if (isActiveMusicPlayer) {
            medioPlayerCreatePlay(data, block)
        }
        ConstantMusic.changePlayPauseMusicLiveData.postValue(Event(isActiveMusicPlayer))
        block.invoke(data)
        startSeekBar()
    }

    private fun getNextMusic(): MusicDataStoradge =
        if (currentMusicPosition < ConstantMusic.getCurrentList().size - 1) ConstantMusic.getCurrentList()[++currentMusicPosition] else ConstantMusic.getCurrentList()
            .first()

    private fun getPrevoiusMusic(): MusicDataStoradge =
        if (currentMusicPosition > 0) ConstantMusic.getCurrentList()[--currentMusicPosition] else ConstantMusic.getCurrentList()
            .last()

    private fun medioPlayerCreatePlay(data: MusicDataStoradge, block: (MusicDataStoradge) -> Unit) {
        mediaPlayer = MediaPlayer.create(mycontext, data.uri)
        currentMusicPosition = ConstantMusic.getCurrentList().indexOf(data)
        mediaPlayer?.let { mdp ->
            mdp.start()
            mdp.setOnCompletionListener {
                if (repeatOne) {
                    stopMediaplayer()
                    start(block)
                } else {
                    nextMusic(block)
                }
            }
        }
    }

    fun setCurrentMusic(block: (MusicDataStoradge) -> Unit) {
        isActiveMusicPlayer = true
        val data = ConstantMusic.getCurrentMusic()
        loadMusicToMusicPlayer(data, block)
        startSeekBar()
    }

    private fun startSeekBar() {
        setCurrentSeekbar().onEach {
            ConstantMusic.loadMusicDurationLiveData.postValue(Event(it))
        }.catch {
            ConstantMusic.loadMusicDurationLiveData.postValue(Event(0))
        }.launchIn(CoroutineScope(Dispatchers.Default))
    }

    private fun setCurrentSeekbar(): Flow<Int> = flow {
        while (isActiveMusicPlayer) {
            emit(mediaPlayer?.currentPosition ?: 0)
            delay(500L)
        }
    }
}