package com.example.musicplayer

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.zZz__utilites.extencions.changeNavigationBarColor
import com.example.musicplayer.zZz__utilites.extencions.changeStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main){
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        changeNavigationBarColor(Color.parseColor("#FF000000"))
        changeStatusBarColor(Color.parseColor("#FF000000"))
    }
}