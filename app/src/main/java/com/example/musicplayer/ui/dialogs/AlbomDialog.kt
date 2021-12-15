package com.example.musicplayer.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.musicplayer.R
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import com.example.musicplayer.databinding.ScreenCreateAlbomDialogBinding

class AlbomDialog(context: Context) {
    @SuppressLint("InflateParams")
    private var view = LayoutInflater.from(context).inflate(R.layout.screen_create_albom_dialog, null, false)
    private var dialog = AlertDialog.Builder(context,R.style.myDialog).setView(view).create()
    private var binding: ScreenCreateAlbomDialogBinding? = ScreenCreateAlbomDialogBinding.bind(view)
    private var defData = EntityAlboms( "Albom", R.drawable.albom)
    private var listenerSave: ((EntityAlboms) -> Unit)? = null
    private var listenerClose: (() -> Unit)? = null

    init {
        binding?.backbutton?.setOnClickListener {
            hide()
        }
        binding?.btnSave?.setOnClickListener {



            defData.name = binding?.albomName?.text?.toString() ?: "ALbom"
            listenerSave?.invoke(defData)
            hide()
        }
        binding?.btnClose?.setOnClickListener {
            listenerClose?.invoke()
            hide()
        }
    }

    fun setListenerSave(block: (EntityAlboms) -> Unit) {
        listenerSave = block
    }

    fun setListenerClose(block: () -> Unit) {
        listenerClose = block
    }

    fun show(data: EntityAlboms? = null) {
        if(data==null){
        when((0..3).random())
        {
            0-> defData.image=R.drawable.albom
            1-> defData.image=R.drawable.albom_second
            2->defData.image=R.drawable.albom_another
            3->defData.image=R.drawable.naushnik_albom
            else -> defData.image=R.drawable.naushnik_albom
        }
        }
        if (data != null) {
            binding?.albomName?.setText(data.name)
            defData.id = data.id
            defData.image = data.image
        }
        dialog.show()
    }

    fun hide() {
        dialog.dismiss()
        binding = null
    }
}