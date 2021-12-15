package com.example.musicplayer.di

import com.example.musicplayer.domen.*
import com.example.musicplayer.domen.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseInjection {
    @Binds
    fun getUseCaseAlbom(useCase: UseCaseAlbomImplament): UseCaseAlbom

    @Binds
    fun getUseCaseAlbomMusicList(usecase: UseCaseAlbomMusicListImplament): UseCaseAlbomMusicList

    @Binds
    fun getUseCaseMainScrin(usecase: UseCaseMainScreenImplament): UseCaseMainScreen

    @Binds
    fun getUseCaseMusicAddToAlbom(useCaseMusic: UseCaseAddMusicToAlbomImplament): UseCaseAddMusicToAlbom

    @Binds
    fun getUseCaseMusicPlayer(useCase: UseCaseMusicPlayerImplament): UseCaseMusicPlayer

    @Binds
    fun getUseCaseSplash(useCase: UseCaseSplashImplament): UseCaseSplash
}