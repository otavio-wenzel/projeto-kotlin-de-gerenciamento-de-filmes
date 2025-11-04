package com.example.cine.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cine.model.entity.Diretor

@Dao
interface DiretorDao {
    @Insert
    suspend fun inserir(diretor: Diretor)
    @Query("SELECT * FROM diretor")
    suspend fun buscarTodos(): List<Diretor>
}