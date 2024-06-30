package com.example.notify.repository

import com.example.notify.db.NoteDatabase
import com.example.notify.models.NoteEntity

class NoteRepository(private val db: NoteDatabase) {

    fun getNotes() = db.getNoteDao().getAllNotes()
    suspend fun insert(note: NoteEntity) = db.getNoteDao().addNote(note)
    suspend fun delete(note: NoteEntity) = db.getNoteDao().deleteNote(note)
    suspend fun update(note: NoteEntity) = db.getNoteDao().updateNote(note)
    fun searchNotes(query: String) = db.getNoteDao().searchNote(query)


}