package com.rmxdev.braillex.presenter.files

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.useCase.repositoryUseCase.getFilesByUserUseCase.GetFilesByUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val filesUseCase: GetFilesByUserUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _selectedFile = MutableStateFlow<Uri?>(null)
    val selectedFile: StateFlow<Uri?> = _selectedFile

    private val _files = MutableStateFlow<List<PdfFile>>(emptyList())
    val files: StateFlow<List<PdfFile>> = _files

    fun selectFile(uri: Uri) {
        _selectedFile.value = uri
    }

    private fun setFiles() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch(Dispatchers.IO) {
            filesUseCase(userId).onSuccess { files ->
                _files.value = files
            }.onFailure {
                _files.value = emptyList()
            }
        }
    }

    init {
        setFiles()
    }
}