package com.rmxdev.braillex.presenter.reproductor

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rmxdev.braillex.data.network.QrCodeGenerator
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.useCase.getGeneratedFilesUseCase.GetGeneratedFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ReproductorViewModel @Inject constructor(
    private val mediaPlayer: MediaPlayer,
    private val firebaseStorage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val qrGenerator: QrCodeGenerator,
): ViewModel() {

    private val _reproductorState = MutableStateFlow<ReproductorState>(ReproductorState.Loading)
    val reproductorState: StateFlow<ReproductorState> = _reproductorState

    fun getQrCodeUrl(pdfFile: PdfFile): String {
        return qrGenerator.generateQrCode("generated_files/${pdfFile.id}")
    }

    fun getFileTitle(pdfFile: PdfFile): String {
        return pdfFile.title
    }

    fun deleteAudioFile(pdfFile: PdfFile, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Eliminar el archivo de Firebase Storage
                val storageRef = firebaseStorage.reference.child("pdfs/${pdfFile.id}.pdf")
                storageRef.delete().await()

                // Eliminar el archivo de audio de Firebase Storage
                val audioStorageRef = firebaseStorage.reference.child("pdfsAudio/${pdfFile.id}.mp3")
                audioStorageRef.delete().await()

                // Eliminar el documento correspondiente en Firestore
                firestore.collection("generated_files").document(pdfFile.id).delete().await()

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