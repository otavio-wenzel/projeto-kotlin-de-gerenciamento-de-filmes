package com.example.cine.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.cine.model.database.AppDatabase
import com.example.cine.ui.theme.FilmesTheme
import com.example.cine.viewmodel.*

class MainActivity : ComponentActivity() {

    private val filmeViewModel: FilmeViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).filmeDao()
        FilmeViewModelFactory(dao)
    }

    private val diretorViewModel: DiretorViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).diretorDao()
        DiretorViewModelFactory(dao)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilmesTheme {
                FilmesHomeScreen(
                    filmeViewModel = filmeViewModel,
                    diretorViewModel = diretorViewModel,
                    onAddDiretor = {
                        startActivity(Intent(this, DiretorActivity::class.java))
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        diretorViewModel.buscarTodos()
    }
}