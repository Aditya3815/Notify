package com.example.notify

import android.app.Application
import com.example.notify.db.NoteDatabase
import com.example.notify.repository.NoteRepository

class NoteApplication : Application() {
    val database: NoteDatabase by lazy { NoteDatabase.invoke(this) }
    val noteRepository: NoteRepository by lazy { NoteRepository(database) }
    override fun onCreate() {
        super.onCreate()

    }
}