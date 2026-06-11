package com.example.examen3.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examen3.model.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios_table WHERE LOWER(nombre) = LOWER(:nombre) LIMIT 1")
    suspend fun obtenerUsuarioPorNombre(nombre: String): Usuario?

    @Query("SELECT * FROM usuarios_table WHERE id = :id LIMIT 1")
    suspend fun obtenerUsuarioPorId(id: Int): Usuario?

    @Query("""
        SELECT * FROM usuarios_table
        WHERE LOWER(nombre) = LOWER(:nombre)
        AND password = :password
        LIMIT 1
    """)
    suspend fun login(nombre: String, password: String): Usuario?

    @Query("SELECT COUNT(*) FROM usuarios_table")
    suspend fun contarUsuarios(): Int

    @Query("UPDATE usuarios_table SET ultimaConexion = :fecha WHERE id = :id")
    suspend fun actualizarUltimaConexion(id: Int, fecha: String)
}