package com.example.musicplayer.zZz__utilites.extencions

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.Toast
import timber.log.Timber

fun Context.showToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Context.getMusicArt(musicImageId: Long): Uri? {
    try {
        val sArtworkUri: Uri = Uri
            .parse("content://media/external/audio/albumart")
        val uri = ContentUris.withAppendedId(sArtworkUri, musicImageId)
        val pfd: ParcelFileDescriptor? = contentResolver
            .openFileDescriptor(uri, "r")
        if (pfd != null) {
            return uri
        }
    } catch (e: Exception) {
        Timber.d(e.message.toString())
    }
    return null
}