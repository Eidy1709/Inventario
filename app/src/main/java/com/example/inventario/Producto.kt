package com.example.inventario

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "inventario") data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val cantidad: Int
)