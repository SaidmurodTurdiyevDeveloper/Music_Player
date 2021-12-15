package com.example.musicplayer.viewModel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.model.EventWithBlock
import com.example.musicplayer.data.source.local.room.entity.EntityAlboms
import com.example.musicplayer.domen.UseCaseAlbom
import com.example.musicplayer.viewModel.ViewModelAlbomScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAlbomImplament @Inject constructor(private var usecase: UseCaseAlbom) : ViewModel(),
    ViewModelAlbomScreen {
    private val _openShuffleLiveData = MutableLiveData<Event<Unit>>()
    override val openShuffleLiveData: LiveData<Event<Unit>> = _openShuffleLiveData

    private val _openFavoruteLiveData = MutableLiveData<Event<Unit>>()
    override val openFavoruteLiveData: LiveData<Event<Unit>> = _openFavoruteLiveData

    private val _openAlbomLiveData = MutableLiveData<Event<Long>>()
    override val openAlbomLiveData: LiveData<Event<Long>> = _openAlbomLiveData

    private val _backLiveData = MutableLiveData<Event<Unit>>()
    override val backLiveData: LiveData<Event<Unit>> = _backLiveData

    private val _loadAlbomItemLiveData = MutableLiveData<Event<List<EntityAlboms>>>()
    override val loadAlbomItemLiveData: LiveData<Event<List<EntityAlboms>>> = _loadAlbomItemLiveData

    private val _updateAlbomItemLiveData =
        MutableLiveData<EventWithBlock<EntityAlboms, EntityAlboms, Unit>>()
    override val updateAlbomItemLiveData: LiveData<EventWithBlock<EntityAlboms, EntityAlboms, Unit>> =
        _updateAlbomItemLiveData

    private val _deleteAlbomItemLiveData =
        MutableLiveData<EventWithBlock<EntityAlboms, EntityAlboms, Unit>>()
    override val deleteAlbomItemLiveData: LiveData<EventWithBlock<EntityAlboms, EntityAlboms, Unit>> =
        _deleteAlbomItemLiveData

    private val _createAlbomItemLiveData =
        MutableLiveData<EventWithBlock<EntityAlboms, EntityAlboms, Unit>>()
    override val createAlbomItemLiveData: LiveData<EventWithBlock<EntityAlboms, EntityAlboms, Unit>> =
        _createAlbomItemLiveData

    private val _showMassageLiveData = MutableLiveData<Event<String>>()
    override val showMassageLiveData: LiveData<Event<String>> = _showMassageLiveData

    init {
        loadAlboms()
    }

    private fun loadAlboms() {
        viewModelScope.launch {
            usecase.getAlbom().onEach {
                _loadAlbomItemLiveData.postValue(Event(it.toMutableList()))
            }.catch {
                _loadAlbomItemLiveData.postValue(Event(emptyList()))
            }.launchIn(viewModelScope)
        }
    }

    override fun back() {
        _backLiveData.postValue(Event(Unit))
    }

    override fun shuffle() {
        viewModelScope.launch {
            _openShuffleLiveData.postValue(Event(Unit))
        }
    }

    override fun favorute() {
        viewModelScope.launch {
            _openFavoruteLiveData.postValue(Event(Unit))
        }
    }

    override fun itemClick(data: EntityAlboms) =
        _openAlbomLiveData.postValue(Event((data.id ?: -1)))


    override fun edit(data: EntityAlboms) {
        _updateAlbomItemLiveData.postValue(EventWithBlock(data) { newData ->
            viewModelScope.launch {
                usecase.update(newData)
                loadAlboms()
            }
        })
    }

    override fun delete(data: EntityAlboms) {
        _deleteAlbomItemLiveData.postValue(EventWithBlock(data) {
            viewModelScope.launch {
                usecase.delete(it)
                loadAlboms()
            }
        })
    }

    override fun createAlbom() {
        val newData = EntityAlboms("", -1)
        _createAlbomItemLiveData.postValue(EventWithBlock(newData) {
            viewModelScope.launch {
                usecase.add(it)
                loadAlboms()
            }
        })
    }
}