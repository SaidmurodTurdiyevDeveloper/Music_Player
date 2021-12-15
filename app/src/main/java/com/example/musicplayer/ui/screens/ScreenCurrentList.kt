package com.example.musicplayer.ui.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.databinding.ScreenCurrentMusicListBinding
import com.example.musicplayer.service.AudioService
import com.example.musicplayer.ui.adapter.MusicAdapter
import com.example.musicplayer.zZz__utilites.MyAppConstants

class ScreenCurrentList:Fragment(R.layout.screen_current_music_list) {
    private var binding:ScreenCurrentMusicListBinding?=null
    private var adapter=MusicAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding=ScreenCurrentMusicListBinding.bind(view)
        binding?.closeBtnCurrent?.setOnClickListener {
            findNavController().navigateUp()
        }
        binding?.listMusicsCurrent?.layoutManager=LinearLayoutManager(requireContext())
        binding?.listMusicsCurrent?.adapter=adapter
        adapter.submitList(ConstantMusic.getCurrentList().toMutableList())
        adapter.setListener {
            val intent = Intent(requireContext(), AudioService::class.java)
            intent.putExtra("Id", it.id)
            intent.action = MyAppConstants.ACTION_CURRENT_MEDIOPLAYER
            startService(intent)
            findNavController().navigateUp()
        }
    }

    private fun startService(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    override fun onDestroy() {
        binding=null
        super.onDestroy()
    }

}