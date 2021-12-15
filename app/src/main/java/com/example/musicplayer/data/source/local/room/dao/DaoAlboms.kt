package com.example.musicplayer.data.source.local.room.dao

import androidx.room.*
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoAlboms {

    @Query("SELECT * From `table_alboms`")
    fun getalbomList(): Flow<List<EntityAlboms>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbom(data: EntityAlboms): Long

    @Update
    suspend fun updateAlbom(taskData: EntityAlboms): Int

    @Delete
    suspend fun delete(taskData: EntityAlboms): Int
}