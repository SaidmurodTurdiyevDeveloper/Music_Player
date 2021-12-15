package com.example.musicplayer.data.source.local.mediaStoradge

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.musicplayer.zZz__utilites.MyAppConstants
import com.example.musicplayer.zZz__utilites.extencions.getMusicArt
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MusicList @Inject constructor(@ApplicationContext private var context: Context) {


    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.TITLE,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Audio.Media.RELATIVE_PATH
        else
            MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ALBUM_ID
    )

    @SuppressLint("Recycle")
    suspend fun init(): Flow<ArrayList<MusicDataStoradge>> = flow {
        val list = ArrayList<MusicDataStoradge>()
        list.clear()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val cursor: Cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        ) ?: return@flow
        while (cursor.moveToNext()) {
            cursor.apply {
                val image = getLong(MyAppConstants.ALBUM_ID)

                val d = MusicDataStoradge(
                    id = getLong(MyAppConstants.ID),
                    artist = getString(MyAppConstants.ARTIST),
                    title = getString(MyAppConstants.TITLE),
                    data = getString(MyAppConstants.DATA),
                    displayName = getString(MyAppConstants.DISPLAY_NAME),
                    duration = getLong(MyAppConstants.DURATION),
                    imageUri = context.getMusicArt(image),
                    uri =ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,getLong(MyAppConstants.ID)) ,
                    favourute = false
                )
                list.add(d)
            }
        }
        emit(list)
    }
}