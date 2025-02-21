package com.rmxdev.braillex.presenter.media

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor() : ViewModel() {

    private var mediaPlayer: MediaPlayer? = null
    var isPrepared = mutableStateOf(false) // Ahora es MutableState

    fun initializePlayer(audioUrl: String) {

        Log.d("MediaPlayer", "Intentando inicializar MediaPlayer con URL: $audioUrl")

       // val validUrl = Uri.decode(audioUrl) // Decodifica por seguridad

      // Log.d("MediaPlayer", "Usando URL: $validUrl") // Verifica la URL en logs

        mediaPlayer?.release() // Libera cualquier instancia previa
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioUrl)
            setOnPreparedListener {
                isPrepared.value = true // Ahora cambia el estado correctamente
                Log.d("MediaPlayer", "Reproducción lista, el usuario puede presionar Play")
            }
            setOnCompletionListener {
                Log.d("MediaPlayer", "Reproducción finalizada")
                isPrepared.value = false
            }
            setOnErrorListener { _, what, extra ->
                Log.e("MediaPlayer", "Error en MediaPlayer: what=$what, extra=$extra")
                false
            }
            prepareAsync()
        }
    }

    fun play() {
        if (isPrepared.value) {
            mediaPlayer?.start()
            Log.d("MediaPlayer", "Audio en reproducción")
        } else {
            Log.d("MediaPlayer", "Intento de reproducir sin que el audio esté listo")
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        Log.d("MediaPlayer", "Audio en pausa")
    }

    fun stop() {
        mediaPlayer?.stop()
        isPrepared.value = false
        Log.d("MediaPlayer", "Audio detenido")
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("MediaPlayer", "MediaPlayer liberado")
    }
}