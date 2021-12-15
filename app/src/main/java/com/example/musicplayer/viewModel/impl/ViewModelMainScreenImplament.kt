package com.example.musicplayer.viewModel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.domen.UseCaseMainScreen
import com.example.musicplayer.service.ServiceHelper
import com.example.musicplayer.viewModel.ViewModelMainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMainScreenImplament @Inject constructor(private var useCaseMainScreen: UseCaseMainScreen) : ViewModel(), ViewModelMainScreen {

    private val _openAlbomLivedata = MutableLiveData<Event<Unit>>()
    override val openAlbomLivedata: MutableLiveData<Event<Unit>> get() = _openAlbomLivedata

    private val _openMusicPlayerLiveData = MutableLiveData<Event<Unit>>()
    override val openMusicPlayerLiveData: MutableLiveData<Event<Unit>> get() = _openMusicPlayerLiveData

    private val _setLikeMusicLivedata = MutableLiveData<Event<Boolean>>()
    override val setLikeMusicLivedata: LiveData<Event<Boolean>> get() = _setLikeMusicLivedata

    private val _setNextLivedata = MutableLiveData<Event<Unit>>()
    override val setNextLivedata: MutableLiveData<Event<Unit>> get() = _setNextLivedata

    private val _setPreviousLivedata = MutableLiveData<Event<Unit>>()
    override val setPreviousLivedata: MutableLiveData<Event<Unit>> get() = _setPreviousLivedata

    private val _showMassageLiveData = MutableLiveData<Event<String>>()
    override val showMassageLiveData: LiveData<Event<String>> get() = _showMassageLiveData

    private val _startServiceCurrentWithCurrentData = MutableLiveData<Event<Unit>>()
    override val setCurrentMusicToService: LiveData<Event<Unit>> = _startServiceCurrentWithCurrentData

    private val _loadAllMusicLivedata = MutableLiveData<Event<List<MusicDataStoradge>>>()
    override val loadAllMusicLivedata: LiveData<Event<List<MusicDataStoradge>>> = _loadAllMusicLivedata

    init {
        _loadAllMusicLivedata.value = Event(ConstantMusic.getList().toMutableList())
    }

    override fun openAlbom() = _openAlbomLivedata.postValue(Event(Unit))

    override fun fovorute() {
        useCaseMainScreen.changeFavaurute().onEach {
            if (it.id != -1L)
                _setLikeMusicLivedata.postValue(Event(it.favourute))
            else
                _showMassageLiveData.postValue(Event("O`zgarish amalga oshirilmadi"))
        }.catch {
            _showMassageLiveData.postValue(Event("Xatolik yuz berdi"))
        }.launchIn(viewModelScope)
    }

    override fun playPaus() {
        ConstantMusic.playPauseMusicLiveData.postValue(Event(!ServiceHelper.isActiveMusicPlayer))
    }

    override fun itemClick(data: MusicDataStoradge) {
        viewModelScope.launch {
            ConstantMusic.setCurrentData(data.id ?: -1)
            ConstantMusic.setCurrentList(ConstantMusic.getList())
            _startServiceCurrentWithCurrentData.postValue(Event(Unit))
            _openMusicPlayerLiveData.postValue(Event(Unit))
        }
    }

    override fun openPlayer() = _openMusicPlayerLiveData.postValue(Event(Unit))

    override fun next() = _setNextLivedata.postValue(Event(Unit))

    override fun previous() = _setPreviousLivedata.postValue(Event(Unit))
}