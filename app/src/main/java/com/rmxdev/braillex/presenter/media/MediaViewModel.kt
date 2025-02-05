package com.rmxdev.braillex.presenter.media

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun playAudio(audioUrl: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                it.start()
                _isPlaying.value = true
            }
        } catch (e: Exception) {
            Log.e("MediaViewModel", "Error playing audio: ${e.localizedMessage}")
        }
    }

    fun pauseAudio() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            _isPlaying.value = false
        }
    }

    fun stopAudio() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            _isPlaying.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}