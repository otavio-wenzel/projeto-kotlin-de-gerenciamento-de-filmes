package com.example.gerenciamentodefilmes.viewmodel


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.gerenciamentodefilmes.model.entity.Filme
import androidx.lifecycle.viewModelScope
import com.example.gerenciamentodefilmes.model.dao.FilmeDao
import kotlinx.coroutines.launch
import com.example.gerenciamentodefilmes.model.entity.Diretor

class FilmeViewModel(private  val filmeDao : FilmeDao): ViewModel() {

    var listaFilmes = mutableStateOf(listOf<Filme>())
        private set

    init {
        carregarFilmes()
    }

    private fun carregarFilmes(){
        viewModelScope.launch {
            listaFilmes.value = filmeDao.buscarTodos()
        }
    }

    fun salvarFilme(titulo: String, diretorId: Int) : String {
        if (titulo.isBlank()) return "Preencha o título do campos!"
        val filme = Filme(id = 0, titulo = titulo, diretorId = diretorId)
        viewModelScope.launch {
            filmeDao.inserir(filme)
            carregarFilmes()
        }
        return "Filme salvo com sucesso!"
    }

    fun excluirFilme(filme: Filme) {
        viewModelScope.launch {
            filmeDao.deletar(filme)
            carregarFilmes()
        }
    }

    fun reinserir(filme: Filme) {
        // usado no Undo do Snackbar
        viewModelScope.launch {
            filmeDao.inserir(filme.copy(id = 0))
            carregarFilmes()
        }
    }

    fun atualizarFilme(id: Int, titulo: String, diretorId: Int) : String {
        if (titulo.isBlank()) return "Não pode deixar o título em branco"
        val atual = listaFilmes.value.find { it.id == id } ?: return "Erro ao atualizar filme"
        val filmeAtualizado = atual.copy(titulo = titulo, diretorId = diretorId)
        viewModelScope.launch {
            filmeDao.atualizar(filmeAtualizado)
            carregarFilmes()
        }
        return "Filme atualizado!"
    }

    suspend fun gerarExemplos(diretores: List<Diretor>) {
        if (diretores.isEmpty()) return
        val pick = { diretores.random().id }
        val exemplos = listOf(
            "Horizontes de Aço",
            "O Vento e a Areia",
            "Ciclos de Outono",
            "A Ponte de Vidro",
            "Maré Noturna"
        ).map { t -> Filme(0, t, pick()) }

        viewModelScope.launch {
            exemplos.forEach { filmeDao.inserir(it) }
            carregarFilmes()
        }
    }
}