package com.example.musicplayer.di

import android.content.Context
import com.example.musicplayer.service.ServiceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ServiceHelperInjection {
    @Provides
    fun getServiceHelper(@ApplicationContext context: Context): ServiceHelper =
        ServiceHelper(context)
}