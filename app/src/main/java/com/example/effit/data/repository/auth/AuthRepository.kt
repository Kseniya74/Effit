package com.example.effit.data.repository.auth

import com.example.effit.data.model.UserModel
import com.example.effit.util.UiState

interface AuthRepository {
    fun signUpUser(email: String, password: String, user: UserModel, result: (UiState<String>) -> Unit)

    fun updateUserInfo(user: UserModel, result: (UiState<String>) -> Unit)

    fun signInUser(email: String, password: String, result: (UiState<String>) -> Unit)

    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)

    fun logout(result: () -> Unit)

    fun storeSession(id: String, result: (UserModel?) -> Unit)

    fun getSession(result: (UserModel?) -> Unit)
}