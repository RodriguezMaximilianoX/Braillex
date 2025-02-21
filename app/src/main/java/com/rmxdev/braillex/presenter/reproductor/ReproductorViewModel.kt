package com.rmxdev.braillex.presenter.reproductor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmxdev.braillex.data.network.QrCodeGenerator
import com.rmxdev.braillex.domain.useCase.reproductorUseCase.deleteAudioUseCase.DeleteAudioUseCase
import com.rmxdev.braillex.domain.useCase.reproductorUseCase.getAudioUrlUseCase.GetAudioUrlUseCase
import com.rmxdev.braillex.domain.useCase.reproductorUseCase.getTitleUseCase.GetTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReproductorViewModel @Inject constructor(
    private val qrGenerator: QrCodeGenerator,
    private val getTitleUseCase: GetTitleUseCase,
    private val getAudioUrlUseCase: GetAudioUrlUseCase,
    private val deleteAudioUseCase: DeleteAudioUseCase
) : ViewModel() {

    private val _audioTitle = MutableStateFlow("")
    val audioTitle: StateFlow<String> = _audioTitle

    private val _qrCodeBitmap = MutableStateFlow<Bitmap?>(null)
    val qrCodeBitmap: StateFlow<Bitmap?> = _qrCodeBitmap

    private val _audioUrl = MutableStateFlow("")
    val audioUrl: StateFlow<String> = _audioUrl

    private val _reproductorState = MutableStateFlow<ReproductorState>(ReproductorState.Initial)
    val reproductorState: StateFlow<ReproductorState> = _reproductorState


    fun loadedData(fieldId: String) {
        viewModelScope.launch {
            if (_reproductorState.value is ReproductorState.Loading) return@launch
            _reproductorState.value = ReproductorState.Loading
            try {
                val titleResult = getTitleUseCase(fieldId)
                _audioTitle.value = titleResult.getOrNull() ?: "Error al obtener el título"

                val audioUrlResult = getAudioUrlUseCase(fieldId)
                _audioUrl.value = audioUrlResult.getOrNull() ?: ""

                if (audioUrlResult.isSuccess) {
                    _qrCodeBitmap.value = qrGenerator.generateQrCode(_audioUrl.value)
                }

                _reproductorState.value = ReproductorState.Success
            } catch (e: Exception) {
                _reproductorState.value = ReproductorState.Error(e.message ?: "Error al cargar los datos")
            }
        }
    }

    fun deleteAudio(fieldId: String) {
        viewModelScope.launch {
            deleteAudioUseCase(fieldId).onSuccess {
                _reproductorState.value = ReproductorState.Deleted
            }.onFailure {
                _reproductorState.value =
                    ReproductorState.Error(it.message ?: "Error al eliminar el audio")
            }
        }
    }

    fun shareQrCode(context: Context) {
        viewModelScope.launch {
            _qrCodeBitmap.value?.let { bitmap ->
                qrGenerator.saveQrToCache(bitmap)?.let { uri ->
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, uri)
                        type = "image/png"
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Compartir código QR"))
                }
            }
        }
    }
}
