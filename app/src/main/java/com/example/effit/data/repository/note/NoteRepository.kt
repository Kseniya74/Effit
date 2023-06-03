package com.example.effit.data.repository.note

import com.example.effit.data.model.Note
import com.example.effit.data.model.UserModel
import com.example.effit.util.UiState

interface NoteRepository {
    fun getNotes(user: UserModel?, result: (UiState<List<Note>>) -> Unit)
    fun addNote(note: Note, result: (UiState<Pair<Note,String>>) -> Unit)
    fun updateNote(note: Note, result: (UiState<String>) -> Unit)
    fun deleteNote(note: Note, result: (UiState<String>) -> Unit)
}