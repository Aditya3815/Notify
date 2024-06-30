package com.example.notify.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notify.databinding.ActivityMainBinding
import com.example.notify.db.NoteDatabase
import com.example.notify.repository.NoteRepository
import com.example.notify.viewmodel.NoteMainViewModel
import com.example.notify.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val noteActivityViewModel: NoteMainViewModel by viewModels {
        NoteViewModelFactory(NoteRepository(NoteDatabase(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


    }
}