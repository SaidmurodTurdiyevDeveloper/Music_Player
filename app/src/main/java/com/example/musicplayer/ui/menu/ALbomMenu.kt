package com.example.musicplayer.ui.menu

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import com.example.musicplayer.R

class ALbomMenu (context: Context, view: View) {
    private var popupMenu = PopupMenu(context, view)
    private var listenerUpdate: (() -> Unit)? = null
    private var listenerDelete: (() -> Unit)? = null

    init {
        popupMenu.menuInflater.inflate(R.menu.albom_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.updateAlbom -> {
                    listenerUpdate?.invoke()
                    popupMenu.dismiss()
                }
                R.id.deleteAlbom -> {
                    listenerDelete?.invoke()
                    popupMenu.dismiss()
                }
            }
            true
        }
    }

    fun submitUpdateListener(block: () -> Unit) {
        listenerUpdate = block
    }

    fun submitDeleteListener(block: () -> Unit) {
        listenerDelete = block
    }

    fun show() {
        if (Build.VERSION.SDK_INT >= 23)
            popupMenu.gravity = Gravity.END
        popupMenu.show()
    }
}