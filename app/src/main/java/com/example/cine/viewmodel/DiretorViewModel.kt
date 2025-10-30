package com.example.gerenciamentodefilmes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciamentodefilmes.model.dao.DiretorDao
import com.example.gerenciamentodefilmes.model.entity.Diretor
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

class DiretorViewModel(private val diretorDao: DiretorDao) : ViewModel() {

    var listaDiretores = mutableStateOf(listOf<Diretor>())

    init {
        buscarTodos()
    }

    fun inserirDiretor(nome: String, anosDeExperiencia: Int) {
        viewModelScope.launch {
            val diretor = Diretor(nome = nome, anosDeExperiencia = anosDeExperiencia)
            diretorDao.inserir(diretor)
            buscarTodos()
        }
    }

    fun buscarTodos() {
        viewModelScope.launch {
            listaDiretores.value = diretorDao.buscarTodos()
        }
    }
}