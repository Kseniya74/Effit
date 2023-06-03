package com.example.effit.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.effit.data.model.UserModel
import com.example.effit.data.repository.auth.AuthRepository
import com.example.effit.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repository: AuthRepository
): ViewModel() {

    private val _signUp = MutableLiveData<UiState<String>>()
    val signUp: LiveData<UiState<String>>
        get() = _signUp

    private val _signIn = MutableLiveData<UiState<String>>()
    val signIn: LiveData<UiState<String>>
        get() = _signIn

    private val _forgotPassword = MutableLiveData<UiState<String>>()
    val forgotPassword: LiveData<UiState<String>>
        get() = _forgotPassword

    val data: MutableLiveData<String> = MutableLiveData()


    fun signUp(
        email: String,
        password: String,
        user: UserModel
    ) {
        _signUp.value = UiState.Loading
        repository.signUpUser(
            email = email,
            password = password,
            user = user
        ) { _signUp.value = it }
    }

    fun signIn(
        email: String,
        password: String
    ) {
        _signIn.value = UiState.Loading
        repository.signInUser(
            email,
            password
        ){
            _signIn.value = it
        }
    }

    fun forgotPassword(email: String) {
        _forgotPassword.value = UiState.Loading
        repository.forgotPassword(email){
            _forgotPassword.value = it
        }
    }

    fun logout(result: () -> Unit){
        repository.logout(result)
    }

    fun getSession(result: (UserModel?) -> Unit){
        repository.getSession(result)
    }
}