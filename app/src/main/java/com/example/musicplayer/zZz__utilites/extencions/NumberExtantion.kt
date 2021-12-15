package com.example.musicplayer.zZz__utilites.extencions


fun Long.getDurationText(): String {
    val min = this / (1000 * 60).toLong()
    val second = (this / 1000 - min * 60)

    return min.toString() + ":" + if (second < 10) {
        "0" + second.toString()
    } else second.toString()
}