package com.example.musicplayer.zZz__utilites.extencions

import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> T.scope(block: T.() -> Unit) {
    block(this)
}