package com.example.musicplayer.zZz__utilites.extencions

fun <T> ArrayList<T>.replaseData(newData: T, block: ((T) -> Boolean)): Boolean {
    for (i in 0 until this.size) {
        if (block(get(i))) {
            remove(get(i))
            set(i, newData)
            return true
        }
    }
    return false
}