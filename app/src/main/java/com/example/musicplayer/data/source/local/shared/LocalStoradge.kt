package com.example.musicplayer.data.source.local.shared

import android.content.Context
import android.content.SharedPreferences

class LocalStoradge(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences("LocalStoradge", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    companion object {
        @Volatile
        private var database: LocalStoradge?=null

        fun getInstance(context: Context): LocalStoradge {
            return database?:synchronized(this) {
                val storadge = LocalStoradge(context)
                database = storadge
                storadge
            }
        }
    }

    fun saveInt(value: Int, key: String) {
        editor.putInt(key, value)
        editor.apply()
    }
    fun saveLong(value: Long, key: String) {
        editor.putLong(key, value)
        editor.apply()
    }

    fun saveString(data: String, key: String) {
        editor.putString(key, data)
        editor.apply()
    }

    fun getInt(key: String): Int = preferences.getInt(key, 0)

    fun getLong(key: String): Long = preferences.getLong(key, -1L)

    fun getString(key: String): String? = preferences.getString(key, null)

}