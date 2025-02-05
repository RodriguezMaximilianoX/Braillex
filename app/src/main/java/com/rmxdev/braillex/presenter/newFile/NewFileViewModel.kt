package com.rmxdev.braillex.presenter.newFile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.useCase.uploadPdfUseCase.UploadPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun uploadFile(uri: Uri, title: String) {
        viewModelScope.launch {
            _pdfFile.value = uploadPdfUseCase(uri, title)
        }
    }
}