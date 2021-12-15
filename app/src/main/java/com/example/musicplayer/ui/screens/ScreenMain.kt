package com.example.musicplayer.ui.screens

import android.content.Intent

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.databinding.ScreenMainBinding
import com.example.musicplayer.service.AudioService
import com.example.musicplayer.ui.adapter.MusicAdapter
import com.example.musicplayer.viewModel.impl.ViewModelMainScreenImplament
import com.example.musicplayer.zZz__utilites.MyAppConstants
import com.example.musicplayer.zZz__utilites.extencions.loadObserverOnlyOnetime
import com.example.musicplayer.zZz__utilites.extencions.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ScreenMain : Fragment(R.layout.screen_main) {
    /*
    * Vareable
    *
    *  */
    private var binding: ScreenMainBinding? = null
    private var adapter = MusicAdapter()
    private val viewmodel: ViewModelMainScreenImplament by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ScreenMainBinding.bind(view)
        loadViewDataListeners()
        binding?.listMusicMain?.layoutManager = LinearLayoutManager(requireContext())
        binding?.listMusicMain?.adapter = adapter
        setObservers()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    /*
    * Observers
    * */
    private val _openAlbomObserver = Observer<Event<Unit>> {
        loadObserverOnlyOnetime(it) {
            findNavController().navigate(ScreenMainDirections.openAlbom())
        }
    }

    private val setCurrentItemObserver = Observer<Event<Unit>> { data ->
        loadObserverOnlyOnetime(data) {
            val intent = Intent(requireContext(), AudioService::class.java)
            intent.action = MyAppConstants.ACTION_CURRENT_MEDIOPLAYER
            startService(intent)
            viewmodel.openPlayer()
        }
    }

    private val _loadItemsObserver = Observer<Event<List<MusicDataStoradge>>> {
        adapter.submitList(it.getDataEveryTime())
    }

    private val _openMusicPlayerObserver = Observer<Event<Unit>> {
        loadObserverOnlyOnetime(it) {
            findNavController().navigate(ScreenMainDirections.openPlayer())
        }
    }

    private val _loadCurrentMusicObserver = Observer<Event<MusicDataStoradge>> {
        val data = it.getDataEveryTime()
        binding?.apply {
            Glide.with(this@ScreenMain)
                .load(data.imageUri)
                .error(R.drawable.music_image).centerCrop()
                .placeholder(R.drawable.music_image)
                .into(imageMusicCurrentMain)
            musicNameMain.text = data.title ?: "None"
            artistNameMain.text = data.artist ?: "None"
        }
        setFavourute(data.favourute)
    }

    private val _setLikeMusicObserver = Observer<Event<Boolean>> { event ->
        loadObserverOnlyOnetime(event) {
            setFavourute(it)
        }
    }

    private val _nextMusicObserver = Observer<Event<Unit>> {
        loadObserverOnlyOnetime(it) {
            val intent = Intent(requireContext(), AudioService::class.java)
            intent.action = MyAppConstants.ACTION_NEXT_MEDIOPLAYER
            startService(intent)
        }
    }

    private val _previousMusicObserver = Observer<Event<Unit>> {
        loadObserverOnlyOnetime(it) {
            val intent = Intent(requireContext(), AudioService::class.java)
            intent.action = MyAppConstants.ACTION_PREVIOUS_MEDIOPLAYER
            startService(intent)
        }
    }

    private val _playPausObserver = Observer<Event<Boolean>> {
        if (it.getDataEveryTime()) {
            val intent = Intent(requireContext(), AudioService::class.java)
            intent.action = MyAppConstants.ACTION_START_MEDIOPLAYER
            startService(intent)
        } else {
            val intent = Intent(requireContext(), AudioService::class.java)
            intent.action = MyAppConstants.ACTION_PAUSE_MEDIOPLAYER
            startService(intent)
        }
    }
    private val _changePlayPausObserver = Observer<Event<Boolean>> {
        if (it.getDataEveryTime()) {
            binding?.btnPlayMusicMain?.setBackgroundResource(R.drawable.ic_paus)
        } else {
            binding?.btnPlayMusicMain?.setBackgroundResource(R.drawable.ic_play)
        }
    }

    private val showMassageObserver = Observer<Event<String>> { event ->
        loadObserverOnlyOnetime(event) {
            requireActivity().showToast(it)
        }
    }

    private fun loadViewDataListeners() {
        binding?.btnNextMusicMain?.setOnClickListener { viewmodel.next() }
        binding?.btnFavoureteMain?.setOnClickListener { viewmodel.fovorute() }
        binding?.btnPlayMusicMain?.setOnClickListener { viewmodel.playPaus() }
        binding?.btnPreviousMain?.setOnClickListener { viewmodel.previous() }
        adapter.setListener { viewmodel.itemClick(it) }
        binding?.currentMusicMain?.setOnClickListener { viewmodel.openPlayer() }
        binding?.openAlbomMain?.setOnClickListener { viewmodel.openAlbom() }
    }

    private fun setObservers() {
        viewmodel.openAlbomLivedata.observe(viewLifecycleOwner, _openAlbomObserver)
        viewmodel.openMusicPlayerLiveData.observe(viewLifecycleOwner, _openMusicPlayerObserver)
        viewmodel.setNextLivedata.observe(viewLifecycleOwner, _nextMusicObserver)
        viewmodel.setPreviousLivedata.observe(viewLifecycleOwner, _previousMusicObserver)
        viewmodel.showMassageLiveData.observe(viewLifecycleOwner, showMassageObserver)
        viewmodel.loadAllMusicLivedata.observe(viewLifecycleOwner, _loadItemsObserver)
        viewmodel.setCurrentMusicToService.observe(viewLifecycleOwner, setCurrentItemObserver)
        viewmodel.setLikeMusicLivedata.observe(viewLifecycleOwner, _setLikeMusicObserver)

        ConstantMusic.changePlayPauseMusicLiveData.observe(viewLifecycleOwner, _changePlayPausObserver)
        ConstantMusic.loadCurrentMusicLiveData.observe(viewLifecycleOwner, _loadCurrentMusicObserver)
        ConstantMusic.playPauseMusicLiveData.observe(viewLifecycleOwner, _playPausObserver)
    }

    private fun startService(intent: Intent) {
        if (ConstantMusic.getCurrentList().size > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().startForegroundService(intent)
            } else {
                requireContext().startService(intent)
            }
        }
    }

    private fun setFavourute(cond: Boolean) {
        if (cond) {
            binding?.btnFavoureteMain?.background?.setTint(Color.parseColor("#B3480E"))
        } else {
            binding?.btnFavoureteMain?.background?.setTint(Color.WHITE)
        }
    }
}