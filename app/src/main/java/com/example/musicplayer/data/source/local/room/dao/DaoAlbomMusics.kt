package com.example.musicplayer.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoAlbomMusics {

    @Query("Select * FROM table_albom_music Where albomId = :id ")
    suspend fun getAlbomMusicList(id: Long): List<EntityAlbomMusics>

    @Query("SELECT * From `table_alboms` Where id = :id Limit 1")
    suspend fun getCurrentAlbom(id: Long): EntityAlboms

    @Delete
    suspend fun deleteMusicFromAlbom(data: EntityAlbomMusics): Int

    @Delete
    suspend fun deleteMusicListFromAlbom(list: List<EntityAlbomMusics>): Int

    @Query("DELETE FROM table_albom_music Where albomId=:albomId and musicId=:musicId ")
    suspend fun deleteMusicWithAlbomIdAndMusicId(albomId: Long, musicId: Long): Int

    @Query("SELECT table_albom_music.* FROM table_albom_music LEFT OUTER JOIN table_alboms ON table_albom_music.albomId = table_alboms.id where table_alboms.id is NULL")
    suspend fun releazeAlbomMusics(): List<EntityAlbomMusics>
}