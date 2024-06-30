package com.example.notify

import android.app.Application
import com.example.notify.db.NoteDatabase
import com.example.notify.repository.NoteRepository

class NoteApplication : Application() {
    private val database by lazy { NoteDatabase.invoke(this) }
    val repository by lazy { NoteRepository(database) }
    override fun onCreate() {
        super.onCreate()
    }
}