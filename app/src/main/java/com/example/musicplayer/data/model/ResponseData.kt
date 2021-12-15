package com.example.musicplayer.data.model

sealed class ResponseData() {
    class Loading() : ResponseData()
    class Error(var message: String) : ResponseData()
    class Open() : ResponseData()
}
