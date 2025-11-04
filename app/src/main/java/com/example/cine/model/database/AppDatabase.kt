package com.example.cine.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cine.model.dao.FilmeDao
import com.example.cine.model.dao.DiretorDao
import com.example.cine.model.entity.Filme
import com.example.cine.model.entity.Diretor

@Database(entities = [Filme::class, Diretor::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmeDao(): FilmeDao
    abstract fun diretorDao(): DiretorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS `diretor` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `nome` TEXT NOT NULL,
                `anosDeExperiencia` INTEGER NOT NULL
            )
        """.trimIndent())

                database.execSQL("""
            ALTER TABLE `filme` ADD COLUMN `diretorId` INTEGER NOT NULL DEFAULT 0
        """.trimIndent())
            }
        }
    }
}