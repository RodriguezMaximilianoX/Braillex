package com.rmxdev.braillex.presenter.media

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.rmxdev.braillex.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    var isPlaying = mutableStateOf(false)
    var isPrepared = mutableStateOf(false)
    var buttonState = mutableIntStateOf(R.drawable.playbutton)
    var buttonDescription = mutableStateOf("Play/Pause")

    fun updateButtonState(action: String) {
        when (action) {
            "playPause" -> {
                buttonState.intValue = if (isPlaying.value) R.drawable.pausebutton else R.drawable.playbutton
                buttonDescription.value = "Play/Pause"
            }
            "volumeUp" -> {
                buttonState.intValue = R.drawable.volumebutton
                buttonDescription.value = "Subir Volumen"
            }
            "volumeDown" -> {
                buttonState.intValue = R.drawable.volumebutton
                buttonDescription.value = "Bajar Volumen"
            }
            "home" -> {
                buttonState.intValue = R.drawable.initialbutton
                buttonDescription.value = "Ir al Inicio"
            }
            "forwardAudio" -> {
                buttonState.intValue = R.drawable.nextbutton
                buttonDescription.value = "Adelantar"
            }
            "backwardAudio" -> {
                buttonState.intValue = R.drawable.previousbutton
                buttonDescription.value = "Retroceder"
            }
        }
    }

    fun initializePlayer(audioUrl: String) {
        val mediaItem = MediaItem.fromUri(audioUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                isPrepared.value = state == Player.STATE_READY
                if (state == Player.STATE_ENDED) {
                    isPlaying.value = false
                    Log.d("ExoPlayer", "Reproducción finalizada")
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                Log.e("ExoPlayer", "Error en ExoPlayer: ${error.message}")
            }
        })
    }

    fun playPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            isPlaying.value = false
            Log.d("ExoPlayer", "Audio en pausa")
        } else if (isPrepared.value) {
            exoPlayer.play()
            isPlaying.value = true
            Log.d("ExoPlayer", "Audio en reproducción")
        }
    }

    fun seekForward() {
        exoPlayer.seekTo(exoPlayer.currentPosition + 5000)
        Log.d("ExoPlayer", "Avanzando 5 segundos")
    }

    fun seekBackward() {
        exoPlayer.seekTo(maxOf(exoPlayer.currentPosition - 5000, 0))
        Log.d("ExoPlayer", "Retrocediendo 5 segundos")
    }

    fun increaseVolume() {
        val newVolume = minOf(exoPlayer.volume + 0.25f, 1f)
        exoPlayer.volume = newVolume
        Log.d("ExoPlayer", "Volumen aumentado a $newVolume")
    }

    fun decreaseVolume() {
        val newVolume = maxOf(exoPlayer.volume - 0.25f, 0f)
        exoPlayer.volume = newVolume
        Log.d("ExoPlayer", "Volumen reducido a $newVolume")
    }

    public override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
        Log.d("ExoPlayer", "ExoPlayer liberado")
    }
}