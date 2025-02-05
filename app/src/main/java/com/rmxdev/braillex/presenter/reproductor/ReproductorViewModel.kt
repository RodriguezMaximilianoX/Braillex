package com.rmxdev.braillex.presenter.reproductor

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rmxdev.braillex.data.network.QrCodeGenerator
import com.rmxdev.braillex.domain.entities.PdfFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// ReproductorViewModel.kt
@HiltViewModel
class ReproductorViewModel @Inject constructor(
    private val mediaPlayer: MediaPlayer,
    private val qrGenerator: QrCodeGenerator
) : ViewModel() {

    private val _reproductorState = MutableStateFlow<ReproductorState>(ReproductorState.Loading)
    val reproductorState: StateFlow<ReproductorState> = _reproductorState

    private lateinit var pdfFile: PdfFile

    fun setPdfFile(file: PdfFile) {
        pdfFile = file
        _reproductorState.value = ReproductorState.Success(pdfFile)
    }

    fun getQrCodeUrl(): String {
        return qrGenerator.generateQrCode("generated_files/${pdfFile.id}")
    }

    fun getFileTitle(): String {
        return pdfFile.title
    }

    fun playAudio() {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(pdfFile.audioUrl)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun deleteAudioFile(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Eliminar el archivo PDF de Firebase Storage
                val pdfStorageRef =
                    FirebaseStorage.getInstance().reference.child("pdfs/${pdfFile.id}.pdf")
                pdfStorageRef.delete().await()

                // Eliminar el archivo de audio de Firebase Storage
                val audioStorageRef =
                    FirebaseStorage.getInstance().reference.child("pdfsAudio/${pdfFile.id}.mp3")
                audioStorageRef.delete().await()

                // Eliminar el documento en Firestore
                FirebaseFirestore.getInstance().collection("generated_files").document(pdfFile.id)
                    .delete().await()

                onComplete(true)
            } catch (e: Exception) {
                Log.e("ReproductorViewModel", "Error deleting file: ${e.localizedMessage}")
                onComplete(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}
