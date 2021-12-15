package com.example.musicplayer.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.musicplayer.data.source.local.room.entity.EntityFavouruteMusics
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoFavouruteMusics {

    @Query("SELECT * FROM table_favourute_music")
    suspend fun getFavouruteMusicList(): List<EntityFavouruteMusics>

    @Insert
    suspend fun insert(data: EntityFavouruteMusics): Long

    @Delete
    suspend fun delete(data: EntityFavouruteMusics): Int

    @Query("DELETE FROM table_favourute_music WHERE musicId=:musicId")
    suspend fun deleteWithAlbomId(musicId: Long)

    @Delete
    suspend fun deleteMusicListFromAlbom(data: List<EntityFavouruteMusics>)

}