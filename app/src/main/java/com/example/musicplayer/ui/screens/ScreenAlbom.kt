package com.example.musicplayer.ui.screens

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
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import com.example.musicplayer.databinding.ScreenAlbomsBinding
import com.example.musicplayer.ui.adapter.AlbomAdapter
import com.example.musicplayer.ui.dialogs.AlbomDialog
import com.example.musicplayer.ui.dialogs.MassageDialog
import com.example.musicplayer.ui.menu.ALbomMenu
import com.example.musicplayer.viewModel.impl.ViewModelAlbomImplament
import com.example.musicplayer.zZz__utilites.MyAppConstants
import com.example.musicplayer.zZz__utilites.extencions.loadObserverOnlyOnetime
import com.example.musicplayer.zZz__utilites.extencions.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ScreenAlbom : Fragment(R.layout.screen_alboms) {
    /*
    * variables
    * */
    private var binding: ScreenAlbomsBinding? = null
    private val viewModel: ViewModelAlbomImplament by viewModels()
    private var adapter = AlbomAdapter()

    /*
    * Observers
    * */

    private var backObservers = Observer<Event<Unit>> { findNavController().navigateUp() }

    private var shuffleObserver = Observer<Event<Unit>> {
        val data = it.getDataOnlyOneTime()
        if (data != null) {
            val a1 = ScreenAlbomDirections.openAlbomList(-1, MyAppConstants.KEY_ALL_MEDIOPLAYER)
            findNavController().navigate(a1)
        }
    }

    private var favoruteObserver = Observer<Event<Unit>> {
        val data = it.getDataOnlyOneTime()
        if (data != null) {
            val a1 = ScreenAlbomDirections.openAlbomList(-1, MyAppConstants.KEY_FAVORUTE_MEDIOPLAYER)
            findNavController().navigate(a1)
        }
    }

    private var openAlbomObserver = Observer<Event<Long>> {
        loadObserverOnlyOnetime(it) {
            Timber.d(it.toString()+" ishladi")
            val a1 = ScreenAlbomDirections.openAlbomList(it, MyAppConstants.KEY_MOVE_ALBOM_ID_MEDIOPLAYER)
            findNavController().navigate(a1)
        }
    }

    private var loadAlbomObserver =
        Observer<Event<List<EntityAlboms>>> {
            adapter.submitList(it.getDataEveryTime())
        }

    private var updateAlbomObserver = Observer<EventWithBlock<EntityAlboms, EntityAlboms, Unit>> { eventWithBlock ->
        val data = eventWithBlock.getDataOnlyOneTime()
        if (data != null) {
            val dialog = AlbomDialog(requireContext())
            dialog.setListenerSave { eventWithBlock.getBlock().invoke(it) }
            dialog.setListenerClose { }
            dialog.show(data)
        }
    }

    private var deleteObserver = Observer<EventWithBlock<EntityAlboms, EntityAlboms, Unit>> {
        val data = it.getDataOnlyOneTime()
        if (data != null) {
            val dialog =
                MassageDialog(requireContext(), "Would you like to delete \"${data.name}\"")
            dialog.setListener { it.getBlock().invoke(data) }
            dialog.show("Delete")
        }
    }

    private var createAlbomObserver = Observer<EventWithBlock<EntityAlboms, EntityAlboms, Unit>> { eventWithBlock ->
        val data = eventWithBlock.getDataOnlyOneTime()
        if (data != null) {
            val dialog = AlbomDialog(requireContext())
            dialog.setListenerSave { eventWithBlock.getBlock().invoke(it) }
            dialog.setListenerClose { }
            dialog.show()
        }
    }

    private val showMassageObserver = Observer<Event<String>> { event ->
        loadObserverOnlyOnetime(event) {
            requireActivity().showToast(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ScreenAlbomsBinding.bind(view)
        loadViewListeners()
        binding?.albomList?.layoutManager = LinearLayoutManager(requireContext())
        binding?.albomList?.adapter = adapter
        loadObserver()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun loadViewListeners() {
        binding?.apply {
            btnShuffle.setOnClickListener { viewModel.shuffle() }
            btnAddAlbom.setOnClickListener { viewModel.createAlbom() }
            btnFavorute.setOnClickListener { viewModel.favorute() }
            closeBtn.setOnClickListener { viewModel.back() }
            adapter.setListener {
                viewModel.itemClick(it)
            }
            adapter.setListenerMore { albomEntity, view ->
                val menu = ALbomMenu(requireContext(), view)
                menu.submitDeleteListener {
                    viewModel.delete(albomEntity)
                }
                menu.submitUpdateListener {
                    viewModel.edit(albomEntity)
                }
                menu.show()
            }

        }
    }

    private fun loadObserver() {
        viewModel.backLiveData.observe(viewLifecycleOwner, backObservers)
        viewModel.openShuffleLiveData.observe(viewLifecycleOwner, shuffleObserver)
        viewModel.openFavoruteLiveData.observe(viewLifecycleOwner, favoruteObserver)
        viewModel.loadAlbomItemLiveData.observe(viewLifecycleOwner, loadAlbomObserver)
        viewModel.deleteAlbomItemLiveData.observe(viewLifecycleOwner, deleteObserver)
        viewModel.updateAlbomItemLiveData.observe(viewLifecycleOwner, updateAlbomObserver)
        viewModel.createAlbomItemLiveData.observe(viewLifecycleOwner, createAlbomObserver)
        viewModel.openAlbomLiveData.observe(viewLifecycleOwner, openAlbomObserver)
        viewModel.showMassageLiveData.observe(viewLifecycleOwner, showMassageObserver)
    }
}