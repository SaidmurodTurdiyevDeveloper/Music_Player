package com.example.musicplayer.viewModel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.model.EventWithBlock
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.domen.UseCaseAlbomMusicList
import com.example.musicplayer.viewModel.ViewModelAlbomMusicList
import com.example.musicplayer.zZz__utilites.MyAppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAlbomMusicListImplament @Inject constructor(private var useCase: UseCaseAlbomMusicList) :
    ViewModel(), ViewModelAlbomMusicList {
    var action = MyAppConstants.KEY_ALL_MEDIOPLAYER
    var albomId = -1L
    private var list = ArrayList<MusicDataStoradge>()
    private val _openAddMusicLiveData = MutableLiveData<Event<Long>>()
    override val openAddMusicLiveData: LiveData<Event<Long>> = _openAddMusicLiveData

    private val _openMusicPlayerLiveData = MutableLiveData<Event<Unit>>()
    override val openMusicPlayerLiveData: LiveData<Event<Unit>> = _openMusicPlayerLiveData

    private val _loadMusicsLiveData = MutableLiveData<Event<List<MusicDataStoradge>>>()
    override val loadMusicsLiveData: LiveData<Event<List<MusicDataStoradge>>> = _loadMusicsLiveData

    private val _backLiveData = MutableLiveData<Event<Unit>>()
    override val backLiveData: LiveData<Event<Unit>> = _backLiveData

    private val _deleteLiveData = MutableLiveData<EventWithBlock<MusicDataStoradge, MusicDataStoradge, Unit>>()
    override val deleteLiveData: LiveData<EventWithBlock<MusicDataStoradge, MusicDataStoradge, Unit>> = _deleteLiveData

    private val _loadAlbomNameLiveData = MutableLiveData<Event<String>>()
    override val loadAlbomNameLiveData: LiveData<Event<String>> = _loadAlbomNameLiveData

    private val _showMassageLiveData = MutableLiveData<Event<String>>()
    override val showMassageLiveData: LiveData<Event<String>> = _showMassageLiveData

    override fun back() {
        _backLiveData.postValue(Event(Unit))
    }

    override fun openMusicPlayer(id: Long) {
        viewModelScope.launch {
            ConstantMusic.setCurrentList(list)
            ConstantMusic.setCurrentData(id)
            _openMusicPlayerLiveData.postValue(Event(Unit))
        }
    }

    override fun delete(data: MusicDataStoradge, albomId: Long) {
        _deleteLiveData.postValue(EventWithBlock(data) {
            viewModelScope.launch {
                useCase.delete(albomId, data.id ?: -1).onEach {
                    if (it) {
                        list.remove(data)
                        _loadMusicsLiveData.postValue(Event(list.toMutableList()))
                    }
                }.catch {
                    _showMassageLiveData.postValue(Event("Wrong! Musics do not remove"))
                }.launchIn(viewModelScope)
            }
        })
    }

    override fun openAddMusicPlayer(albomId: Long) {
        _openAddMusicLiveData.postValue(Event(albomId))
    }

    override fun loadData() {
        viewModelScope.launch {
            useCase.loadList(albomId, action).onEach {
                list.clear()
                list.addAll(it)
                _loadMusicsLiveData.postValue(Event(list.toMutableList()))
            }.catch {
                _loadMusicsLiveData.postValue(Event(emptyList()))
                _showMassageLiveData.postValue(Event("Wrong! Musics do not load"))
            }.launchIn(viewModelScope)
        }
    }
}