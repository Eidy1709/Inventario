package com.example.inventario

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface ProductoDao {
    @Query("SELECT * FROM inventario")
    suspend fun obtenerTodos(): List<Producto>
    @Insert
    suspend fun insertar(producto: Producto)
}