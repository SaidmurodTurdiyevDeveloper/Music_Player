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
import com.example.musicplayer.data.model.HelperData
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import com.example.musicplayer.databinding.ScreenAddMusicToAlbomBinding
import com.example.musicplayer.ui.adapter.CheckAdapter
import com.example.musicplayer.viewModel.impl.ViewModelAddMusicToAlbomImplament
import com.example.musicplayer.zZz__utilites.extencions.loadObserverOnlyOnetime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenAddMusicToAlbom : Fragment(R.layout.screen_add_music_to_albom) {
    private var binding: ScreenAddMusicToAlbomBinding? = null
    private var adapter = CheckAdapter()
    private var albomId = 0L
    private val viewmodel: ViewModelAddMusicToAlbomImplament by viewModels()
    private var list = ArrayList<EntityAlbomMusics>()
    private var submitList = ArrayList<HelperData<Boolean, MusicDataStoradge>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = ScreenAddMusicToAlbomBinding.bind(view)
        getAlbomId()
        loadViewListeners()
        registerObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private var backObserver = Observer<Event<Unit>> {
        loadObserverOnlyOnetime(it) {
            findNavController().navigateUp()
        }
    }

    private var _loadListObserver = Observer<Event<List<HelperData<Boolean, MusicDataStoradge>>>> {
        submitList.clear()
        submitList.addAll(it.getDataEveryTime())
        adapter.submitList(submitList)
    }

    private fun registerObserver() {
        viewmodel.backLiveData.observe(viewLifecycleOwner, backObserver)
        viewmodel.loadMusicLiveData.observe(viewLifecycleOwner, _loadListObserver)
    }

    private fun loadViewListeners() {
        binding?.backButtonAdd?.setOnClickListener {
            viewmodel.close()
        }
        binding?.doneAdd?.setOnClickListener {
            viewmodel.done(list)
        }
        adapter.setListener { data, postion ->
            val d = HelperData(false, data)
            if (!isExist(data)) {
                d.element = true
                list.add(EntityAlbomMusics( albomId, d.data.id?:-1))
            }
            submitList.removeAt(postion)
            submitList.add(postion, d)
            adapter.submitList(submitList)
        }
        binding?.listAddMusic?.layoutManager = LinearLayoutManager(requireContext())
        binding?.listAddMusic?.adapter = adapter
    }

    private fun getAlbomId() {
        albomId = requireArguments().getLong("Id")
    }

    private fun isExist(data: MusicDataStoradge): Boolean {
        var cond = false
        var i = 0
        while (i < list.size) {
            if (data.id == list[i].musicId) {
                cond = true
                list.removeAt(i)
                i = list.size
            }
            i++
        }
        return cond
    }
}