package com.example.effit.data.repository.auth

import android.content.SharedPreferences
import com.example.effit.data.model.UserModel
import com.example.effit.util.FireStoreCollection
import com.example.effit.util.SharedPrefConstants
import com.example.effit.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class AuthRepositoryImpl(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore,
    val appPreferences: SharedPreferences,
    val gson: Gson
) : AuthRepository {
    override fun signUpUser(
        email: String,
        password: String,
        user: UserModel,
        result: (UiState<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user.id = it.result.user?.uid ?: ""
                    updateUserInfo(user) { state ->
                        when (state) {
                            is UiState.Success -> {
                                storeSession(id = it.result.user?.uid ?: "") {
                                    if (it == null)
                                        result.invoke(UiState.Failure("Регистрация пользователя прошла успешно, но сеанс не удалось сохранить"))
                                    else
                                        result.invoke(UiState.Success("Регистрация пользователя прошла успешно!"))
                                }
                            }

                            is UiState.Failure ->
                                result.invoke(UiState.Failure(state.error))

                            else -> {}
                        }
                    }
                } else {
                    try {
                        throw it.exception ?: java.lang.Exception("Неверная аутентификация")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(UiState.Failure("Аутентификация не удалась, пароль должен быть не менее 6 символов"))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failure("Ошибка аутентификации, введен неверный адрес электронной почты"))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(UiState.Failure("Аутентификация не удалась, электронная почта уже зарегистрирована."))
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun updateUserInfo(user: UserModel, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.USER).document(user.id)
        document
            .set(user)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Данные пользователя успешно обновлены")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun signInUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeSession(id = task.result.user?.uid ?: "") {
                        if (it == null)
                            result.invoke(UiState.Failure("Не удалось сохранить локальный сеанс"))
                        else
                            result.invoke(UiState.Success("Вход совершён успешно"))
                    }
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Ошибка аутентификации, проверьте электронную почту и пароль"))
            }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    result.invoke(UiState.Success("Письмо было отправлено"))
                else
                    result.invoke(UiState.Failure(task.exception?.message))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Ошибка аутентификации, проверьте электронную почту"))
            }
    }

    override fun logout(result: () -> Unit) {
        auth.signOut()
        appPreferences.edit().putString(SharedPrefConstants.USER_SESSION, null).apply()
        result.invoke()
    }

    override fun storeSession(id: String, result: (UserModel?) -> Unit) {
        database.collection(FireStoreCollection.USER).document(id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObject(UserModel::class.java)
                    appPreferences.edit()
                        .putString(SharedPrefConstants.USER_SESSION, gson.toJson(user)).apply()
                    result.invoke(user)
                } else
                    result.invoke(null)
            }
            .addOnFailureListener {
                result.invoke(null)
            }
    }

    override fun getSession(result: (UserModel?) -> Unit) {
        val userStr = appPreferences.getString(SharedPrefConstants.USER_SESSION, null)
        if (userStr == null) {
            result.invoke(null)
        } else {
            val user = gson.fromJson(userStr, UserModel::class.java)
            result.invoke(user)
        }
    }
}