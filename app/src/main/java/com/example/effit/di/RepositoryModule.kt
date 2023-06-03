package com.example.effit.di

import android.content.SharedPreferences
import com.example.effit.data.repository.auth.AuthRepository
import com.example.effit.data.repository.auth.AuthRepositoryImpl
import com.example.effit.data.repository.note.NoteRepository
import com.example.effit.data.repository.note.NoteRepositoryImpl
import com.example.effit.data.repository.profile.ProfileRepository
import com.example.effit.data.repository.profile.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        database: FirebaseFirestore,
        storageReference: StorageReference
    ): NoteRepository {
        return NoteRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
        gson: Gson
    ): AuthRepository {
        return AuthRepositoryImpl(auth,database,appPreferences,gson)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
    ): ProfileRepository {
        return ProfileRepositoryImpl(auth,database)
    }
}