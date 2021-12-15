package com.example.musicplayer.app

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.musicplayer.BuildConfig
import com.example.musicplayer.zZz__utilites.MyAppConstants
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotiFicationChanel()
        createNotiFicationChanel2()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    companion object {
        lateinit var instance: Application
    }

    private fun createNotiFicationChanel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channelName = "My Background Service"
            val chan = NotificationChannel(MyAppConstants.channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val manager = (getSystemService(NotificationManager::class.java) as NotificationManager)
            manager.createNotificationChannel(chan)
        }
    }

    private fun createNotiFicationChanel2() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channelName = "My Service"
            val chan = NotificationChannel(MyAppConstants.channelID2, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = (getSystemService(NotificationManager::class.java) as NotificationManager)
            manager.createNotificationChannel(chan)
        }
    }
}