package com.example.musicplayer.service

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.musicplayer.R
import com.example.musicplayer.zZz__utilites.MyAppConstants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import android.app.Notification
import android.os.Build
import android.provider.MediaStore
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import com.example.musicplayer.app.App
import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge


@AndroidEntryPoint
class AudioService : Service() {
    @Inject
    lateinit var serviceHelper: ServiceHelper
    override fun onBind(p0: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeground(data: MusicDataStoradge?) {
        try {
            val notificationBuilder = NotificationCompat.Builder(this, MyAppConstants.channelID)
            val mediasesion = MediaSessionCompat(this, "MediPlayer")
            val bitmap = MediaStore.Images.Media.getBitmap(App.instance.contentResolver, data?.imageUri)
            val notification: Notification = notificationBuilder
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(data?.title)
                .setContentText(data?.artist + " - " + data?.displayName)
                .setLargeIcon(bitmap)
                .addAction(
                    R.drawable.ic_previous_play,
                    "Previous",
                    createPendingIntent(MyAppConstants.ACTION_PREVIOUS_MEDIOPLAYER)
                )
                .addAction(
                    if (ServiceHelper.isActiveMusicPlayer) {
                        R.drawable.ic_paus
                    } else {
                        R.drawable.ic_play
                    }, "PlayPaus",
                    createPendingIntent(if (ServiceHelper.isActiveMusicPlayer) MyAppConstants.ACTION_PAUSE_MEDIOPLAYER else MyAppConstants.ACTION_START_MEDIOPLAYER)
                )
                .addAction(
                    R.drawable.ic_next_play,
                    "Next",
                    createPendingIntent(MyAppConstants.ACTION_NEXT_MEDIOPLAYER)
                )
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediasesion.sessionToken)
                        .setShowActionsInCompactView(0, 1, 2)
                )
                .setSilent(true)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .build()
            startForeground(2, notification)
        } catch (e: Exception) {
            Timber.d(e.message.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startNotification(data: MusicDataStoradge?) {
        try {
            val notificationBuilder = NotificationCompat.Builder(this, MyAppConstants.channelID2)
            val mediasesion = MediaSessionCompat(this, "MediPlayer")
            val bitmap = MediaStore.Images.Media.getBitmap(App.instance.contentResolver, data?.imageUri)
            val notification: Notification = notificationBuilder
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(data?.title)
                .setContentText(data?.artist + " - " + data?.displayName)
                .setLargeIcon(bitmap)
                .addAction(
                    R.drawable.ic_previous_play,
                    "Previous",
                    createPendingIntent(MyAppConstants.ACTION_PREVIOUS_MEDIOPLAYER)
                )
                .addAction(
                    if (ServiceHelper.isActiveMusicPlayer) {
                        R.drawable.ic_paus
                    } else {
                        R.drawable.ic_play
                    }, "PlayPaus",
                    createPendingIntent(if (ServiceHelper.isActiveMusicPlayer) MyAppConstants.ACTION_PAUSE_MEDIOPLAYER else MyAppConstants.ACTION_START_MEDIOPLAYER)
                )
                .addAction(
                    R.drawable.ic_next_play,
                    "Next",
                    createPendingIntent(MyAppConstants.ACTION_NEXT_MEDIOPLAYER)
                )
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediasesion.sessionToken)
                        .setShowActionsInCompactView(0, 1, 2)
                )
                .setSilent(true)
                .setSubText("Music Player")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
            val notify = getSystemService(NotificationManager::class.java)
            notify.notify(2, notification)
        } catch (e: Exception) {
            Timber.d(e.message.toString())
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createPendingIntent(serviceAction: String): PendingIntent {
        val intent = Intent(this, AudioService::class.java)
        intent.action = serviceAction
        return PendingIntent.getService(
            this, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            doAction(intent)
        } catch (e: Exception) {
            Timber.d(e.message.toString())
        }
        return START_NOT_STICKY
    }

    private fun doAction(intent: Intent?) {
        when (intent?.action) {
            MyAppConstants.ACTION_CURRENT_MEDIOPLAYER -> {
                serviceHelper.setCurrentMusic() {
                    if (Build.VERSION.SDK_INT >= 26)
                        startForeground(it)
                }
            }
            MyAppConstants.ACTION_DURATION_MEDIOPLAYER -> {
                val duration = intent.getIntExtra(MyAppConstants.KEY_DURATION_ITEM, 0)
                serviceHelper.setDuration(duration)
            }
            MyAppConstants.ACTION_PAUSE_MEDIOPLAYER -> {
                serviceHelper.paus {
                    if (Build.VERSION.SDK_INT >= 26) {
                        /**
                         * i must notification this place
                         */
                        stopForeground(true)
                        startNotification(ConstantMusic.getCurrentMusic())
                    }
                }
            }
            MyAppConstants.ACTION_START_MEDIOPLAYER -> {
                serviceHelper.start {
                    if (Build.VERSION.SDK_INT >= 26)
                        startForeground(it)
                }
            }
            MyAppConstants.ACTION_STOP_MEDIOPLAYER -> {
                serviceHelper.stopMediaplayer()
            }
            MyAppConstants.ACTION_NEXT_MEDIOPLAYER -> {
                serviceHelper.nextMusic {
                    if (Build.VERSION.SDK_INT >= 26)
                        startForeground(it)
                }
            }
            MyAppConstants.ACTION_PREVIOUS_MEDIOPLAYER -> {
                serviceHelper.prevousMusic {
                    if (Build.VERSION.SDK_INT >= 26)
                        startForeground(it)
                }
            }
        }
    }

    override fun onDestroy() {
        serviceHelper.stopMediaplayer()
        if (Build.VERSION.SDK_INT >= 26)
            stopForeground(true)
        super.onDestroy()
    }
}