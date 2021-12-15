package com.example.musicplayer.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicplayer.data.source.local.room.dao.*
import com.example.musicplayer.data.source.local.room.entity.*

@Database(
    entities = [EntityAlboms::class, EntityFavouruteMusics::class, EntityAlbomMusics::class], version = 1
)
abstract class DatabaseMusic : RoomDatabase() {
    abstract fun getDaoAlboms(): DaoAlboms
    abstract fun getDaoAlbomMusics(): DaoAlbomMusics
    abstract fun getDaoAddMusicToAlboms(): DaoAddMusicToAlboms
    abstract fun getFavoruteMusicDao(): DaoFavouruteMusics

    companion object {
        @Volatile
        private var INSTANCE: DatabaseMusic? = null

        fun init(context: Context): DatabaseMusic {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseMusic::class.java,
                    "MusicPlayer"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}