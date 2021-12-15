package com.example.musicplayer.data.model

open class Event<T>(private val data: T) {
    private var condition = true
    private var conditionlistener = true
    fun getDataOnlyOneTime(): T? {
        return if (condition) {
            condition = false
            data
        } else null
    }

    fun getDataEveryTime(): T = data
}