package com.example.musicplayer.viewModel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.ConstantMusic
import com.example.musicplayer.data.model.Event
import com.example.musicplayer.data.source.local.mediaStoradge.MusicDataStoradge
import com.example.musicplayer.domen.UseCaseMusicPlayer
import com.example.musicplayer.service.ServiceHelper
import com.example.musicplayer.viewModel.ViewModelMusicPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewModelMusicPlayerImplament @Inject constructor(private var useCase: UseCaseMusicPlayer) :
    ViewModel(), ViewModelMusicPlayer {

    private val _nextMuiscLiveData = MutableLiveData<Event<Unit>>()
    override val nextMuiscLiveData: LiveData<Event<Unit>> get() = _nextMuiscLiveData

    private val _previousMuiscLiveData = MutableLiveData<Event<Unit>>()
    override val previousMuiscLiveData: LiveData<Event<Unit>> get() = _previousMuiscLiveData

    private val _aboutInfoMusicLiveData = MutableLiveData<Event<MusicDataStoradge>>()
    override val aboutInfoMuiscLiveData: LiveData<Event<MusicDataStoradge>> get() = _aboutInfoMusicLiveData

    private val _repeatMuiscLiveData = MutableLiveData<Event<Boolean>>()
    override val repeatMuiscLiveData: LiveData<Event<Boolean>> get() = _repeatMuiscLiveData

    private val _shuffleMuiscLiveData = MutableLiveData<Event<Boolean>>()
    override val shuffleMuiscLiveData: LiveData<Event<Boolean>> get() = _shuffleMuiscLiveData

    private val _openListLivedata = MutableLiveData<Event<Unit>>()
    override val openListLivedata: LiveData<Event<Unit>> get() = _openListLivedata

    private val _setLikeMusicLivdata = MutableLiveData<Event<Boolean>>()
    override val setLikeMusicLivdata: LiveData<Event<Boolean>> get() = _setLikeMusicLivdata

    private val _showMassageLiveData = MutableLiveData<Event<String>>()
    override val showMassageLiveData: LiveData<Event<String>> get() = _showMassageLiveData

    init {
        viewModelScope.launch {
            postRepeatOrShuffleChange(true)
            postRepeatOrShuffleChange(false)
        }
    }

    override fun playPaus() {
        ConstantMusic.playPauseMusicLiveData.postValue(Event(!ServiceHelper.isActiveMusicPlayer))
    }

    override fun next() = _nextMuiscLiveData.postValue(Event(Unit))

    override fun previous() = _previousMuiscLiveData.postValue(Event(Unit))

    override fun shuffle() {
        viewModelScope.launch {
            useCase.setShuffle()
            postRepeatOrShuffleChange(false)
        }
    }

    override fun repeat() {
        viewModelScope.launch {
            useCase.setRepeat()
            postRepeatOrShuffleChange(true)
        }
    }

    override fun info() = _aboutInfoMusicLiveData.postValue(Event(ConstantMusic.getCurrentMusic()))

    override fun like() {
        viewModelScope.launch {
            useCase.favourute().onEach {
                _setLikeMusicLivdata.postValue(Event(it))
            }.catch {
                _showMassageLiveData.postValue(Event("Wrong!"))
            }.launchIn(viewModelScope)
        }
    }

    override fun openList() = _openListLivedata.postValue(Event(Unit))

    private suspend fun postRepeatOrShuffleChange(isRepeat: Boolean) {
        if (isRepeat) {
            useCase.getRepeat().onEach {
                Timber.d("repeat come $it")
                _repeatMuiscLiveData.postValue(Event(it))
            }.catch {
                _showMassageLiveData.postValue(Event("Repeat Wrong!"))
            }.launchIn(viewModelScope)
        } else {
            useCase.getShuffle().onEach {
                Timber.d("shufle come $it")
                _shuffleMuiscLiveData.postValue(Event(it))
            }.catch {
                _showMassageLiveData.postValue(Event("Shuffle Wrong!"))
            }.launchIn(viewModelScope)
        }
    }
}