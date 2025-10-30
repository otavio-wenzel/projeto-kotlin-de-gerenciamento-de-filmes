package com.example.gerenciamentodefilmes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gerenciamentodefilmes.model.dao.DiretorDao

class DiretorViewModelFactory(
    private val diretorDao: DiretorDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiretorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiretorViewModel(diretorDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}