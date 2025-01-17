package com.rmxdev.braillex.presenter.signup

sealed class SignUpState {
    data object Idle : SignUpState()
    data object Loading : SignUpState()
    data object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}