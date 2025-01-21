package com.rmxdev.braillex.presenter.files

import com.rmxdev.braillex.domain.entities.PdfFile

sealed class FileState {
    object Idle: FileState()
    object Loading: FileState()
    data class Success(val files: List<PdfFile>): FileState()
    data class Error(val message: String): FileState()
}