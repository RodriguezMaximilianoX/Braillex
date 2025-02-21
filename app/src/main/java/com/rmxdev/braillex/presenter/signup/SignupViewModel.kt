package com.rmxdev.braillex.presenter.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmxdev.braillex.domain.useCase.userUseCase.registerUseCase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: RegisterUseCase
): ViewModel() {

    private val _signupState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signupState: StateFlow<SignUpState> = _signupState

    fun registerUser(email: String, password: String){
        if(email.isNotBlank() && password.isNotBlank()){
            viewModelScope.launch {
                _signupState.value = SignUpState.Loading
                _signupState.value = signupUseCase(email, password)
                    .fold(
                        onSuccess = { SignUpState.Success },
                        onFailure = { SignUpState.Error(it.message ?: "Unknown error") }
                    )
            }
        }
    }
}