package com.example.effit.data.repository.profile

import com.example.effit.data.model.UserModel
import com.example.effit.util.FireStoreCollection
import com.example.effit.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProfileRepositoryImpl(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore
) : ProfileRepository {

    private val currentUser = Firebase.auth.currentUser
    private val currentUserDocRef: DocumentReference?
        get() = currentUser?.let { database.document(it.uid) }

    override fun editUser(
        email: String,
        password: String,
        name: String,
        user: UserModel,
        result: (UiState<String>) -> Unit
    ) {
            currentUser?.updateProfile(userProfileChangeRequest {
                updateUserInfo(user) { state ->
                    when(state){
                        is UiState.Success -> {
                            result.invoke(
                                UiState.Success("Данные обновлены успешно!")
                            )
                        }

                        is UiState.Failure -> {
                            result.invoke(UiState.Failure(state.error))
                        }

                        else -> {}
                    }
                }
            })
                ?.addOnCompleteListener {
                    if (it.isSuccessful){
                    }
                }
                ?.addOnFailureListener {
                    result.invoke(
                        UiState.Failure(
                            it.localizedMessage
                        )
                    )
                }
        }

    override fun updateUserInfo(
        user: UserModel,
        result: (UiState<String>) -> Unit
    ) {
        val document = currentUser?.let { database.collection(FireStoreCollection.USER).document(it.uid) }
        document?.set(user)?.addOnSuccessListener {
            result.invoke(
                UiState.Success("Пользователь был успешно обновлён")
            )
        }?.addOnFailureListener {
            result.invoke(
                UiState.Failure(
                    it.localizedMessage
                )
            )
        }
    }

    override fun showProfile(onComplete: (UserModel) -> Unit) {

        currentUserDocRef?.get()
            ?.addOnSuccessListener {
                it.toObject(UserModel::class.java)?.let { it1 -> onComplete(it1) }
            }

    }
}