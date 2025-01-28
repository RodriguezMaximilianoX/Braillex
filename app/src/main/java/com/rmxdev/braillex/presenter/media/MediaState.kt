package com.rmxdev.braillex.presenter.media

sealed class MediaState {
    object Loading : MediaState()
    data class Success(val audioUrl: String) : MediaState()
    data class Error(val message: String) : MediaState()
}