package com.example.musicplayer.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_albom_music")
data class EntityAlbomMusics(
    var albomId: Long,
    var musicId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
