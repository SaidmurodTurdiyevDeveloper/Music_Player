package com.example.musicplayer.data.model

import com.example.musicplayer.zZz__utilites.AnyListener

open class EventWithBlock<T, Input, Output>(data: T, private val block: AnyListener<Input, Output>) : Event<T>(data) {
    fun getBlock(): AnyListener<Input, Output> = block
}