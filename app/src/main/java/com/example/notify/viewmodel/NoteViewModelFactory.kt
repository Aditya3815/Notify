package com.example.notify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notify.repository.NoteRepository

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteMainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteMainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}