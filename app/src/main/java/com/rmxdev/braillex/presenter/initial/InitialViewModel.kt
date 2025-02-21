package com.rmxdev.braillex.presenter.initial

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(): ViewModel() {
    private val _scannedQrContent = MutableStateFlow<String?>(null)
    val scannedQrContent: StateFlow<String?> = _scannedQrContent

    fun processScannedQr(qrContent: String) {
            Log.d("InitialViewModel", "processScannedQr: Updating scannedQrContent with $qrContent")
            _scannedQrContent.value = qrContent
    }
}