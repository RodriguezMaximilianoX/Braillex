package com.rmxdev.braillex.presenter.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmxdev.braillex.domain.useCase.userUseCase.loginUseCase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun loginUser(email: String, password: String){
        if(email.isNotBlank() && password.isNotBlank()){
            viewModelScope.launch {
                _loginState.value = LoginState.Loading
                _loginState.value = loginUseCase(email, password)
                    .fold(
                        onSuccess = { LoginState.Success },
                        onFailure = { LoginState.Error(it.message ?: "Unknown error") }
                    )
            }
        }
    }
}