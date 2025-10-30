package com.example.gerenciamentodefilmes.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diretor")
data class Diretor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val anosDeExperiencia: Int
)