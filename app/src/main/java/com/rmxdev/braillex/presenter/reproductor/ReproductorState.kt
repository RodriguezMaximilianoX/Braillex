package com.rmxdev.braillex.presenter.reproductor

import com.rmxdev.braillex.domain.entities.PdfFile

sealed class ReproductorState {
    object Loading : ReproductorState()
    data class Success(val pdfFile: PdfFile) : ReproductorState()
    data class Error(val message: String) : ReproductorState()
}