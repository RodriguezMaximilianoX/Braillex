package com.rmxdev.braillex.presenter.media

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.rmxdev.braillex.R
import com.rmxdev.braillex.domain.useCase.mediaUseCase.GetPublicityAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val useCase: GetPublicityAudioUseCase
) : ViewModel() {

    var isPlaying = mutableStateOf(false)
    var isPrepared = mutableStateOf(false)
    var buttonState = mutableIntStateOf(R.drawable.playbutton)
    var buttonDescription = mutableStateOf("Play/Pause")

    fun updateButtonState(action: String) {
        when (action) {
            "playPause" -> {
                buttonState.intValue =
                    if (isPlaying.value) R.drawable.pausebutton else R.drawable.playbutton
                buttonDescription.value = "Play/Pause"
            }

            "home" -> {
                buttonState.intValue = R.drawable.initialbutton
                buttonDescription.value = "Ir al Inicio"
            }
        }
    }

    fun initializePlayer(audioUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val publicidadAudioUrl = useCase()

            withContext(Dispatchers.Main) {
                exoPlayer.clearMediaItems()

                val mediaItems = mutableListOf<MediaItem>()

                publicidadAudioUrl?.let {
                    mediaItems.add(MediaItem.fromUri(it))
                }

                mediaItems.add(MediaItem.fromUri(audioUrl))

                exoPlayer.setMediaItems(mediaItems)
                exoPlayer.prepare()
                exoPlayer.play() // 🔥 Inicia la reproducción automáticamente
                isPlaying.value = true
                buttonState.intValue = R.drawable.pausebutton // 🔁 Botón actualizado
                buttonDescription.value = "Play/Pause"

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
        }
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

    fun stop() {
        exoPlayer.stop()
    }
}