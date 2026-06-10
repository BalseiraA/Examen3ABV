package com.example.examen3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("usuarios_table")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int= 0,
    val nombre: String,
    val password: String
