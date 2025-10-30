package com.example.gerenciamentodefilmes.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "filme",
    foreignKeys = [ForeignKey(
        entity = Diretor::class,
        parentColumns = ["id"],
        childColumns = ["diretorId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Filme(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    var titulo: String,

    @ColumnInfo(name = "diretorId")
    val diretorId: Int
)