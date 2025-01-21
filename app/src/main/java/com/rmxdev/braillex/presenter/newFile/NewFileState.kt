package com.rmxdev.braillex.presenter.newFile

import com.rmxdev.braillex.domain.entities.PdfFile

sealed class NewFileState {
    object Idle : NewFileState()
    object Loading : NewFileState()
    data class Success(val files: PdfFile) : NewFileState()
    data class Error(val message: String) : NewFileState()
}