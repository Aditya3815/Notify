package com.example.notify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notify.models.NoteEntity
import com.example.notify.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteMainViewModel(private val repository: NoteRepository) : ViewModel() {

    fun saveNote(note: NoteEntity) = viewModelScope.launch {
        repository.insert(note)
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch {
        repository.delete(note)
    }

    fun getAllNotes(): LiveData<List<NoteEntity>> = repository.getNotes()

    fun updateNote(note: NoteEntity) = viewModelScope.launch {
        repository.update(note)
    }

    fun searchNote(query: String?): LiveData<List<NoteEntity>> {
        return if (query == null) {
            getAllNotes()
        } else {
            repository.searchNotes(query)
        }

    }




}