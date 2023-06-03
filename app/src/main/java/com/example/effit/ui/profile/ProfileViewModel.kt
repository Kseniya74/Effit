package com.example.effit.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.effit.data.model.UserModel
import com.example.effit.data.repository.profile.ProfileRepository
import com.example.effit.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: ProfileRepository,
    val database: FirebaseFirestore,
    val auth: FirebaseAuth
) : ViewModel() {

    val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val currentUser = Firebase.auth.currentUser
    private val currentUserDocRef: DocumentReference?
        get() = firestoreInstance.document("user/${FirebaseAuth.getInstance().uid
            ?: throw NullPointerException("UID is null")}")

    private val _updateUser = MutableLiveData<UiState<String>>()
    val updateUser: LiveData<UiState<String>>
        get() = _updateUser

    private val _showUser = MutableLiveData<UiState<String>>()
    val showUser: LiveData<UiState<String>>
        get() = _showUser

    val data: MutableLiveData<String> = MutableLiveData()

    fun editUser(
        email: String,
        password: String,
        name: String,
        user: UserModel
    ) {
        _updateUser.value = UiState.Loading
        repository.editUser(
            email = email,
            password = password,
            name = name,
            user = user
        ) { _updateUser.value = it }
    }

    fun showProfile(onComplete: (UserModel) -> Unit) {

        currentUserDocRef?.get()
            ?.addOnSuccessListener {
                it.toObject(UserModel::class.java)?.let { it1 -> onComplete(it1) }
            }

    }
}