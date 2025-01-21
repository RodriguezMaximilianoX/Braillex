package com.rmxdev.braillex.presenter.newFile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmxdev.braillex.domain.useCase.uploadPdfUseCase.UploadPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewFileVewModel @Inject constructor(
    private val uploadPdf: UploadPdfUseCase
) : ViewModel() {
    private val _newFileState = MutableStateFlow<NewFileState>(NewFileState.Idle)
    val newFileState: StateFlow<NewFileState> = _newFileState

    fun processPdf(fileUri: String, title: String) {
        viewModelScope.launch {
            _newFileState.value = NewFileState.Loading
            val uri = Uri.parse(fileUri)
            val result = uploadPdf(uri, title)
            _newFileState.value = result.fold(
                onSuccess = { NewFileState.Success(it) },
                onFailure = { NewFileState.Error(it.message ?: "Unknown error") }
            )
        }
    }

}