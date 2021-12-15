package com.example.musicplayer.ui.screens

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.databinding.ScreenPlayingMusicBinding
import com.example.musicplayer.service.AudioService
import com.example.musicplayer.service.ServiceHelper
import com.example.musicplayer.ui.dialogs.MassageDialog
import com.example.musicplayer.viewModel.impl.ViewModelMusicPlayerImplament
import com.example.musicplayer.zZz__utilites.MyAppConstants
import com.example.musicplayer.zZz__utilites.extencions.getDurationText
import com.example.musicplayer.zZz__utilites.extencions.loadObserverOnlyOnetime
import com.example.musicplayer.zZz__utilites.extencions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenMusicPlayer : Fragment(R.layout.screen_playing_music) {

    private var binding: ScreenPlayingMusicBinding? = null
    private val viewmodel: ViewModelMusicPlayerImplament by viewModels()
    private var currentData: MusicDataStoradge? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ScreenPlayingMusicBinding.bind(view)
        loadViewListener()
        setObservers()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private val openListObserver = Observer<Event<Unit>> {
        loadObserverOnlyOnetime(it) {
            findNavController().navigate(ScreenMusicPlayerDirections.openMusicList())
        }
    }

    private val _nextMusicObserver = Observer<Event<Unit>> {
        loadObserverOnlyOnetime(it) {
            val intent = Intent(requireContext(), AudioService::class.java)
            intent.action = MyAppConstants.ACTION_NEXT_MEDIOPLAYER
            startService(intent)
            startAnimation(ServiceHelper.isActiveMusicPlayer)
        }
    }

    private val _previousMusicObserver = Observer<Event<Unit>> {
        loadObserverOnlyOnetime(it) {
            val intent = Intent(requireContext(), AudioService::class.java)
            intent.action = MyAppConstants.ACTION_PREVIOUS_MEDIOPLAYER
            startService(intent)
            startAnimation(ServiceHelper.isActiveMusicPlayer)
        }
    }

    private val _aboutInfoMuiscObserver = Observer<Event<MusicDataStoradge>> { data ->
        loadObserverOnlyOnetime(data) {
            val dialog =
                MassageDialog(requireContext(), "Name:" + it.title + "\n \nArtist:${it.artist}")
            dialog.setListener { dialog.hide() }
            dialog.show("Close")
        }
    }

    private val _repeatMuiscObserver = Observer<Event<Boolean>> {
        if (it.getDataEveryTime()) {
            binding?.btnReviewPlaying?.setBackgroundResource(R.drawable.ic_repeat)
            binding?.btnReviewPlaying?.background?.setTint(Color.parseColor("#B3480E"))
        } else {
            binding?.btnReviewPlaying?.setBackgroundResource(R.drawable.ic_repeat_all)
            binding?.btnReviewPlaying?.background?.setTint(Color.WHITE)
        }
        ServiceHelper.changeRepeat(it.getDataEveryTime())
    }

    private val _shuffleMuiscObserver = Observer<Event<Boolean>> {
        if (it.getDataEveryTime()) {
            binding?.btnShufflePlaying?.background?.setTint(Color.parseColor("#B3480E"))
        } else {
            binding?.btnShufflePlaying?.background?.setTint(Color.WHITE)
        }
        ServiceHelper.changeShuffle(it.getDataEveryTime())
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
            binding?.btnPlayMusicPlaying?.setBackgroundResource(R.drawable.ic_paus)
            startAnimation(true)
        } else {
            binding?.btnPlayMusicPlaying?.setBackgroundResource(R.drawable.ic_play)
            startAnimation(false)
        }
    }

    private val _loadMusicInfoObserver = Observer<Event<MusicDataStoradge>> {
        val data = it.getDataEveryTime()
        currentData = data
        if (binding != null) {
            binding?.apply {
                Glide.with(this@ScreenMusicPlayer)
                    .load(data.imageUri)
                    .error(R.drawable.music_image).centerCrop()
                    .placeholder(R.drawable.music_image)
                    .into(musicImagePlaying)
            }
            binding?.musicNamePlaying?.text = data.title
            binding?.artistNamePlaying?.text = data.artist
            binding?.textLastdurationPlaying?.text = data.duration?.getDurationText()
            binding?.seekbarPlaying?.max = data.duration?.toInt() ?: 0
            setFavourute(data.favourute)
        }
    }

    private val _setSeekbarDurationObserver = Observer<Event<Int>> {
        if (Build.VERSION.SDK_INT >= 24)
            binding?.seekbarPlaying?.setProgress(it.getDataEveryTime(), true)
        else
            binding?.seekbarPlaying?.progress = it.getDataEveryTime()
        binding?.textCurrentDurationPlaying?.text = it.getDataEveryTime().toLong().getDurationText()

    }

    private val _setLikeMusicObserver = Observer<Event<Boolean>> { event ->
        loadObserverOnlyOnetime(event) {
            setFavourute(it)
        }
    }

    private val showMassageObserver = Observer<Event<String>> { event ->
        loadObserverOnlyOnetime(event) {
            requireActivity().showToast(it)
        }
    }

    /*
    * Private function
    * */
    private fun startService(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    private fun setObservers() {
        viewmodel.aboutInfoMuiscLiveData.observe(viewLifecycleOwner, _aboutInfoMuiscObserver)
        viewmodel.nextMuiscLiveData.observe(viewLifecycleOwner, _nextMusicObserver)
        viewmodel.previousMuiscLiveData.observe(viewLifecycleOwner, _previousMusicObserver)
        viewmodel.repeatMuiscLiveData.observe(viewLifecycleOwner, _repeatMuiscObserver)
        viewmodel.shuffleMuiscLiveData.observe(viewLifecycleOwner, _shuffleMuiscObserver)
        viewmodel.openListLivedata.observe(viewLifecycleOwner, openListObserver)
        viewmodel.setLikeMusicLivdata.observe(viewLifecycleOwner, _setLikeMusicObserver)
        viewmodel.showMassageLiveData.observe(viewLifecycleOwner, showMassageObserver)

        ConstantMusic.changePlayPauseMusicLiveData.observe(viewLifecycleOwner, _changePlayPausObserver)
        ConstantMusic.loadCurrentMusicLiveData.observe(viewLifecycleOwner, _loadMusicInfoObserver)
        ConstantMusic.playPauseMusicLiveData.observe(viewLifecycleOwner, _playPausObserver)
        ConstantMusic.loadMusicDurationLiveData.observe(
            viewLifecycleOwner,
            _setSeekbarDurationObserver
        )
    }

    private fun loadViewListener() {
        binding?.btnPlayMusicPlaying?.setOnClickListener {
            viewmodel.playPaus()
        }
        binding?.btnNextMusicPlaying?.setOnClickListener {
            viewmodel.next()
        }
        binding?.btnPreviosMusicPlaying?.setOnClickListener {
            viewmodel.previous()
        }
        binding?.btnReviewPlaying?.setOnClickListener {
            viewmodel.repeat()
        }
        binding?.btnShufflePlaying?.setOnClickListener {
            viewmodel.shuffle()
        }
        binding?.backButtonPlaying?.setOnClickListener {
            findNavController().navigateUp()
        }
        binding?.infoMusicPlaying?.setOnClickListener {
            if (currentData != null)
                viewmodel.info()
        }
        binding?.likeMusicPlaying?.setOnClickListener {
            if (currentData != null)
                viewmodel.like()
        }

        binding?.listButtonPlaying?.setOnClickListener {
            viewmodel.openList()
        }

        binding?.seekbarPlaying?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                ServiceHelper.isActiveMusicPlayer = false
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                ServiceHelper.isActiveMusicPlayer = true
                val intent = Intent(requireContext(), AudioService::class.java)
                intent.action = MyAppConstants.ACTION_DURATION_MEDIOPLAYER
                intent.putExtra(MyAppConstants.KEY_DURATION_ITEM, p0?.progress ?: 0)
                startService(intent)
            }
        })

        startAnimation(ServiceHelper.isActiveMusicPlayer)
    }

    private fun setFavourute(cond: Boolean) {
        if (cond) {
            binding?.likeMusicPlaying?.background?.setTint(Color.parseColor("#B3480E"))
        } else {
            binding?.likeMusicPlaying?.background?.setTint(Color.WHITE)
        }
    }

    private fun startAnimation(cond: Boolean) {
        if (cond) {
            binding?.musicImagePlaying?.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.rotation
                )
            )
        } else
            binding?.musicImagePlaying?.clearAnimation()
    }
}