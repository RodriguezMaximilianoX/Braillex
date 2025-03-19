package com.rmxdev.braillex.presenter.newFile

import android.net.Uri
import androidx.compose.material3.CircularProgressIndicator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.useCase.repositoryUseCase.uploadPdfUseCase.UploadPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewFileViewModel @Inject constructor(
    private val uploadPdfUseCase: UploadPdfUseCase
) : ViewModel() {
    private val _pdfFile = MutableStateFlow<Result<PdfFile>?>(null)
    val pdfFile: StateFlow<Result<PdfFile>?> = _pdfFile

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    fun uploadFile(uri: Uri, title: String) {
        _isUploading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _pdfFile.value = uploadPdfUseCase(uri, title)
            _isUploading.value = false
        }
    }
    fun resetUploadResult() {
        _pdfFile.value = null
    }
}