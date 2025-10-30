package com.example.cine.model.dao

import androidx.room.*
import com.example.cine.model.entity.Filme

@Dao
interface FilmeDao {

    @Insert
    suspend fun inserir(filme: Filme)

    @Query("SELECT * FROM filme")
    suspend fun buscarTodos(): List<Filme>

    @Delete
    suspend fun deletar(filme: Filme)

    @Update
    suspend fun atualizar(filme: Filme)
}