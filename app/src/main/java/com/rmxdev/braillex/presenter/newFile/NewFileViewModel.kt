package com.rmxdev.braillex.presenter.newFile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmxdev.braillex.domain.useCase.getFileUriUseCase.GetFileFromUriUseCase
import com.rmxdev.braillex.domain.useCase.getFileUriUseCase.GetFileUriUseCase
import com.rmxdev.braillex.domain.useCase.uploadPdfUseCase.UploadPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewFileVewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val uploadPdf: UploadPdfUseCase,
    private val getFileFromUri: GetFileFromUriUseCase,
    private val getFileUri: GetFileUriUseCase
) : ViewModel() {
    private val _newFileState = MutableStateFlow<NewFileState>(NewFileState.Idle)
    val newFileState: StateFlow<NewFileState> = _newFileState

    fun processPdf(fileUri: String, title: String) {
        val uri = Uri.parse(fileUri)
        val file = getFileFromUri(applicationContext, uri) // Obtener el archivo con getFileFromUri

        if (file != null) {
            val fileUriToUpload = getFileFromUri(applicationContext, uri) // Obtener la URI para subir el archivo

            viewModelScope.launch {
                _newFileState.value = NewFileState.Loading
                val uriToUpload = getFileUri(fileUriToUpload, applicationContext)
                val result = uploadPdf(uriToUpload, title, applicationContext)
                _newFileState.value = result.fold(
                    onSuccess = { NewFileState.Success(it) },
                    onFailure = { NewFileState.Error(it.message ?: "Unknown error") }
                )
            }
        } else {
            _newFileState.value = NewFileState.Error("Error al obtener el archivo")
        }
    }

}