package com.rmxdev.braillex.presenter.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmxdev.braillex.domain.useCase.getGeneratedFilesUseCase.GetGeneratedFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val listFiles: GetGeneratedFilesUseCase
) : ViewModel() {

    private val _fileState = MutableStateFlow<FileState>(FileState.Idle)
    val fileState: StateFlow<FileState> = _fileState

    fun fetchListFiles(){

    }

}