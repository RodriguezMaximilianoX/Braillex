package com.rmxdev.braillex.presenter.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rmxdev.braillex.domain.useCase.userUseCase.deleteAccountUseCase.DeleteAccountUseCase
import com.rmxdev.braillex.presenter.signup.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val deleteAccountUseCase: DeleteAccountUseCase
): ViewModel() {

    private val _deleteAccountState = MutableStateFlow<DeleteAccountState>(DeleteAccountState.Idle)
    val deleteAccountState: StateFlow<DeleteAccountState> = _deleteAccountState

    fun logout( onSignOut: () -> Unit ) {
        auth.signOut()
        onSignOut()
    }
    fun deleteAccount() {
        viewModelScope.launch {
            _deleteAccountState.value = DeleteAccountState.Loading
            try {
                deleteAccountUseCase()
                _deleteAccountState.value = DeleteAccountState.Success
            } catch (e: Exception) {
                _deleteAccountState.value = DeleteAccountState.Error(e.message ?: "Error desconocido")
            }
        }
    }
    fun resetState(){
        _deleteAccountState.value = DeleteAccountState.Idle
    }
}