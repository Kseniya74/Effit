package com.example.effit.data.repository.profile

import com.example.effit.data.model.UserModel
import com.example.effit.util.UiState

interface ProfileRepository {
    fun updateUserInfo(user: UserModel, result: (UiState<String>) -> Unit)

    fun editUser(email: String, password: String, name: String, user: UserModel, result: (UiState<String>) -> Unit)

    fun showProfile(onComplete: (UserModel) -> Unit)
}