package com.example.musicplayer.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_alboms")
data class EntityAlboms(
    var name: String,
    var image: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
