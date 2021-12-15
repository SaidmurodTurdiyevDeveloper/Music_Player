package com.example.musicplayer.viewModel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.model.HelperData
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.data.source.local.room.entity.EntityAlbomMusics
import com.example.musicplayer.domen.UseCaseAddMusicToAlbom
import com.example.musicplayer.viewModel.ViewModelAddMusicToAlbom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelAddMusicToAlbomImplament @Inject constructor(private var useCase: UseCaseAddMusicToAlbom) :
    ViewModel(), ViewModelAddMusicToAlbom {

    private val _backLiveData = MutableLiveData<Event<Unit>>()
    override val backLiveData: MutableLiveData<Event<Unit>> = _backLiveData

    private val _loadMusicLiveData = MutableLiveData<Event<List<HelperData<Boolean, MusicDataStoradge>>>>()
    override val loadMusicLiveData: LiveData<Event<List<HelperData<Boolean, MusicDataStoradge>>>> =
        _loadMusicLiveData

    private val _showMassageLiveData = MutableLiveData<Event<String>>()
    override val showMassageLiveData: LiveData<Event<String>> = _showMassageLiveData

    init {
        useCase.loadList().onEach {
            _loadMusicLiveData.postValue(Event(it.toMutableList()))
        }.catch {
            _showMassageLiveData.postValue(Event("Wrong! Musics do not load"))
        }.launchIn(viewModelScope)
    }

    override fun done(list: List<EntityAlbomMusics>) {
        viewModelScope.launch {
            Timber.d(list.size.toString())
            useCase.saveList(list).onEach {
                if (it) {
                    _backLiveData.postValue(Event(Unit))
                } else {
                    _showMassageLiveData.postValue(Event("Musics can not save"))
                }
            }.catch {
                _showMassageLiveData.postValue(Event("Wrong! Musics do not save"))
            }.launchIn(viewModelScope)
        }
    }

    override fun close() = _backLiveData.postValue(Event(Unit))
}