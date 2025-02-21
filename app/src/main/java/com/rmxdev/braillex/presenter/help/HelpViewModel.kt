package com.rmxdev.braillex.presenter.help

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
): ViewModel() {
    fun logout( onSignOut: () -> Unit ) {
        auth.signOut()
        onSignOut()
    }
}