package com.example.musicplayer.ui.screens

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.viewModel.impl.ViewModelSplashImplament
import com.example.musicplayer.zZz__utilites.extencions.loadObserverOnlyOnetime
import com.example.musicplayer.zZz__utilites.extencions.showToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ScreenSplash : Fragment(R.layout.screen_splash) {

    private val viewmodel: ViewModelSplashImplament by viewModels()

    private val showMassageObserver = Observer<Event<String>> { event ->
        loadObserverOnlyOnetime(event) {
            requireActivity().showToast(it)
        }
    }
    private val openScreenMainObserver = Observer<Event<Unit>> {
        loadObserverOnlyOnetime(it) {
            findNavController().navigate(ScreenSplashDirections.openMain())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadObserver()
        getPermission()
        Timber.d("Takkj")
    }

    private fun loadObserver() {
        viewmodel.openMainScreenLiveData.observe(viewLifecycleOwner, openScreenMainObserver)
        viewmodel.showMassageLiveData.observe(viewLifecycleOwner, showMassageObserver)
    }

    private fun getPermission() {
        Timber.d("Ishladi")
        Dexter.withContext(requireContext())
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    viewmodel.loadMusics()
                }
                override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
                    p1?.continuePermissionRequest()
                }
            }).check()
    }
}