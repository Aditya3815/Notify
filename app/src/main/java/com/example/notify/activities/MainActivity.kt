package com.example.notify.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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

        // Set status bar color to white and ensure light icons
        window.apply {
            // Clear any translucent flags
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // Add flags to draw system bar backgrounds
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // Set status bar color to white
            statusBarColor = Color.WHITE
            // Ensure status bar icons are dark (for light status bar)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}
