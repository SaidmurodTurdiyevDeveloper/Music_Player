package com.example.musicplayer.ui.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.model.EventWithBlock
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.databinding.ScreenMusicListBinding
import com.example.musicplayer.service.AudioService
import com.example.musicplayer.ui.adapter.MusicAdapter
import com.example.musicplayer.ui.dialogs.MassageDialog
import com.example.musicplayer.ui.menu.AlbomMusicsMenu
import com.example.musicplayer.viewModel.impl.ViewModelAlbomMusicListImplament
import com.example.musicplayer.zZz__utilites.MyAppConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenMusicList : Fragment(R.layout.screen_music_list) {
    private var binding: ScreenMusicListBinding? = null
    private val viewModel: ViewModelAlbomMusicListImplament by viewModels()
    private var adapter = MusicAdapter()

    /*
    * observer
    * */

    private val openAddMusicObserver = Observer<Event<Long>> {
        val data = it.getDataOnlyOneTime()
        if (data != null) {
            findNavController().navigate(ScreenMusicListDirections.openAddMusicScreen(data))
        }
    }

    private val openMusicPlayerObserver = Observer<Event<Unit>> {
        val data = it.getDataOnlyOneTime()
        if (data != null) {
            val intent = Intent(requireContext(), AudioService::class.java)
            intent.action = MyAppConstants.ACTION_CURRENT_MEDIOPLAYER
            startService(intent)
            findNavController().navigate(ScreenMusicListDirections.openMusic())
        }
    }

    private val loadMusicsObserver = Observer<Event<List<MusicDataStoradge>>> {
        adapter.submitList(it.getDataEveryTime())
    }

    private val backObserver = Observer<Event<Unit>> {
        findNavController().navigateUp()
    }

    private val deleteObserver = Observer<EventWithBlock<MusicDataStoradge, MusicDataStoradge, Unit>> {
        val data = it.getDataOnlyOneTime()
        val listener = it.getBlock()
        if (data != null) {
            val dialog = MassageDialog(
                requireContext(),
                "Would you like to delete \"${data.title}\" from albom"
            )
            dialog.setListener {
                listener.invoke(data)
            }
            dialog.show("Delete")
        }
    }

    private val loadAlbomNameObserver = Observer<Event<String>> {
        binding?.actionBarText?.text = it.getDataEveryTime()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ScreenMusicListBinding.bind(view)
        getAlbomid()
        loadViewListner()
        binding?.listMusics?.layoutManager = LinearLayoutManager(requireContext())
        binding?.listMusics?.adapter = adapter
        setObserver()
        viewModel.loadData()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


    private fun startService(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    private fun loadViewListner() {
        binding?.closeBtn?.setOnClickListener { viewModel.back() }
        adapter.setListener {
            it.id?.let { it1 -> viewModel.openMusicPlayer(it1) }
        }
        adapter.submitMenu { musicEntity, view ->
            if (viewModel.action == MyAppConstants.KEY_MOVE_ALBOM_ID_MEDIOPLAYER) {
                val menu = AlbomMusicsMenu(requireContext(), view)
                menu.submitDeleteListener {
                    viewModel.delete(musicEntity, viewModel.albomId)
                }
                menu.show()
            }
        }
    }

    private fun setObserver() {
        viewModel.backLiveData.observe(viewLifecycleOwner, backObserver)
        viewModel.deleteLiveData.observe(viewLifecycleOwner, deleteObserver)
        viewModel.loadAlbomNameLiveData.observe(viewLifecycleOwner, loadAlbomNameObserver)
        viewModel.openAddMusicLiveData.observe(viewLifecycleOwner, openAddMusicObserver)
        viewModel.openMusicPlayerLiveData.observe(viewLifecycleOwner, openMusicPlayerObserver)
        viewModel.loadMusicsLiveData.observe(viewLifecycleOwner, loadMusicsObserver)
    }

    private fun getAlbomid() {
        val arguments = requireArguments()
        viewModel.albomId = arguments.getLong("Id")
        viewModel.action = arguments.getString("Move") ?: MyAppConstants.KEY_ALL_MEDIOPLAYER
        when (viewModel.action) {
            MyAppConstants.KEY_MOVE_ALBOM_ID_MEDIOPLAYER -> {
                binding?.addMusic?.visibility = View.VISIBLE
                binding?.actionBarText?.text = "Album"
                binding?.addMusic?.setOnClickListener { viewModel.openAddMusicPlayer(viewModel.albomId) }
            }
            MyAppConstants.KEY_FAVORUTE_MEDIOPLAYER -> {
                binding?.actionBarText?.text = "Favourute"
                binding?.addMusic?.visibility = View.INVISIBLE
            }
            MyAppConstants.KEY_ALL_MEDIOPLAYER -> {
                binding?.actionBarText?.text = "Shuffle"
                binding?.addMusic?.visibility = View.INVISIBLE
            }
            else -> {
                binding?.actionBarText?.text = "Error"
                binding?.addMusic?.visibility = View.INVISIBLE
            }
        }
    }
}