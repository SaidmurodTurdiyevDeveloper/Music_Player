package com.example.musicplayer.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_favourute_music")
data class EntityFavouruteMusics(
    var musicId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}