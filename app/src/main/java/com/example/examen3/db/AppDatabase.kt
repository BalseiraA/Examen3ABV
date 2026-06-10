package com.example.examen3.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.examen3.converters.Converters
import com.example.examen3.dao.UsuarioDao
import com.example.examen3.model.Usuario

@Database(entities = [Usuario::class], version= 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
}