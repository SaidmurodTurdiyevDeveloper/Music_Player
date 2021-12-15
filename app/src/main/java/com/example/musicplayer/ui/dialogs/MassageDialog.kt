package com.example.musicplayer.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ShowMessageBinding

class MassageDialog(context: Context, message: String) {
    @SuppressLint("InflateParams")
    private var view = LayoutInflater.from(context).inflate(R.layout.show_message, null, false)
    private var dialog = AlertDialog.Builder(context, R.style.myDialog).setView(view).create()
    private var binding: ShowMessageBinding? = ShowMessageBinding.bind(view)
    private var listener: (() -> Unit)? = null

    init {
        binding?.backbutton?.setOnClickListener {
            dialog.dismiss()
            binding = null
        }
        binding?.actionButton?.setOnClickListener {
            listener?.invoke()
            dialog.dismiss()
            binding = null
        }
        binding?.text?.text = message
    }

    fun setListener(block: () -> Unit) {
        listener = block
    }

    fun show(buttonText: String) {
        binding?.actionButton?.text = buttonText
        dialog.show()
    }

    fun hide() {
        dialog.dismiss()
        binding = null
    }
}
