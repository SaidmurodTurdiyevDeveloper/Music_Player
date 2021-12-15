package com.example.musicplayer.zZz__utilites.extencions

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.musicplayer.data.model.Event

fun Fragment.putArguments(block: Bundle.() -> Unit): Fragment {
    val bundle = arguments ?: Bundle()
    block(bundle)
    arguments = bundle
    return this
}
fun <T>Fragment.loadObserverOnlyOnetime(data:Event<T>,block: (T) -> Unit){
    val newData = data.getDataOnlyOneTime()
    if (newData != null) {
        block.invoke(newData)
    }
}