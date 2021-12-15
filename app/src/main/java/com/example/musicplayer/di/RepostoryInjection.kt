package com.example.musicplayer.di

import com.example.musicplayer.data.repostory.*
import com.example.musicplayer.domen.repostory.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepostoryInjection {
    @Binds
    fun getMainRepostory(repostory: RepostoryMainScreenImplament): RepostoryMainScreen

    @Binds
    fun getAlbomRepostory(repostory: RepostoryAlbomImplament): RepostoryAlbom

    @Binds
    fun getAlbomMusicListRepostory(repostory: RepostoryAlbomMusicListImplament): RepostoryAlbomMusicList

    @Binds
    fun getMusicPlayerRepostory(repostory: RepostoryMusicPlayerImplament): RepostoryMusicPlayer

    @Binds
    fun getAddMuscToAlbomRepostory(repostory: RepostoryMusicAddToAlbomImplament): RepostoryMusicAddToAlbom

    @Binds
    fun getSplashRepsitory(repostorySplashImplament: RepostorySplashImplament): RepostorySplash
}