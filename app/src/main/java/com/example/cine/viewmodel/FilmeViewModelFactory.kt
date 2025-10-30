package com.example.gerenciamentodefilmes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gerenciamentodefilmes.model.dao.FilmeDao

class FilmeViewModelFactory(
    private val filmeDao: FilmeDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmeViewModel::class.java)) {
            return FilmeViewModel(filmeDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}