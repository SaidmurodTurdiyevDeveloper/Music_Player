package com.example.musicplayer.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics

@Dao
interface DaoAddMusicToAlboms {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setMusicToAlbom(list: List<EntityAlbomMusics>)
}