package com.rmxdev.braillex.presenter.reproductor

import com.rmxdev.braillex.domain.entities.PdfFile

sealed class ReproductorState {
    data object Initial : ReproductorState()
    data object Loading : ReproductorState()
    data object Success : ReproductorState()
    data object Deleted: ReproductorState()
    data class Error(val message: String) : ReproductorState()
}