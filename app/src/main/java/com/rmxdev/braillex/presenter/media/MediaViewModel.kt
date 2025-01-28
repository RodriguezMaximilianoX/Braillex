package com.rmxdev.braillex.presenter.media

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.useCase.getGeneratedFilesUseCase.GetGeneratedFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val generatedFile: GetGeneratedFilesUseCase,
    private val mediaPlayer: MediaPlayer,
): ViewModel() {

    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Loading)
    val mediaState: StateFlow<MediaState> = _mediaState

    fun loadFile(audioUrl: String){
        viewModelScope.launch {
            generatedFile(audioUrl).onSuccess {
                _mediaState.value = MediaState.Success(audioUrl)
                playAudio(audioUrl)
            }.onFailure {
                _mediaState.value = MediaState.Error(it.message ?: "Error desconocido")
            }
        }
    }

    fun playAudio(audioUrl: String){
        mediaPlayer.reset()
        mediaPlayer.setDataSource(audioUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { it.start() }
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.setOnErrorListener { _, _, _ -> false }
    }

}