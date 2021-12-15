package com.example.musicplayer.data.source.local.mediaStoradge

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicDataStoradge(
    var id: Long? = null,
    var artist: String? = null,
    var title: String? = null,
    var data: String? = null,
    var displayName: String? = null,
    var duration: Long? = null,
    var imageUri: Uri? = null,
    var uri: Uri?=null,
    var favourute: Boolean = false,
) : Parcelable
