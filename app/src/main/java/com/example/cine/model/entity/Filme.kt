package com.example.cine.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update


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