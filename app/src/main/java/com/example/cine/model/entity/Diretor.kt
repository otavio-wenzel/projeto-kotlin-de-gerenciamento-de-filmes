package com.example.cine.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update


@Entity(tableName = "diretor")
data class Diretor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val anosDeExperiencia: Int
)