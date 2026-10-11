package com.example.inventario

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 1. Vincular los componentes
        val botonCargar = findViewById<Button>(R.id.button_id)
        val textoResultado = findViewById<TextView>(R.id.text_view_id)

        // 2. Inicializar la instancia de Room
        val database = AppDatabase.getDatabase(this)

        // 3. Configurar la acción al presionar el botón
        botonCargar.setOnClickListener {
            // Abrimos un bloque asíncrono seguro (Corrutina)
            lifecycleScope.launch {
                // Con esto ejecutamos el proceso pesado fuera del hilo principal
                val listaProductos = withContext(Dispatchers.IO) {
                    // PASO DE PRUEBA: Si la base de datos está completamente vacía,
                    // insertamos dos productos automáticos para tener qué mostrar.
                    if (database.productoDao().obtenerTodos().isEmpty()) {
                        database.productoDao().insertar(Producto(nombre = "Arroz 1kg", cantidad = 25))
                        database.productoDao().insertar(Producto(nombre = "Aceite de Oliva", cantidad = 12))
                    }

                    // Ejecutamos el SELECT real de SQLite
                    database.productoDao().obtenerTodos()
                }

                // 4. Volvemos al hilo principal de forma automática y mostramos los datos
                if (listaProductos.isEmpty()) {
                    textoResultado.text = "El inventario está vacío."
                } else {
                    val stringBuilder = StringBuilder()
                    for (producto in listaProductos) {
                        stringBuilder.append("📦 ID: ${producto.id} | ${producto.nombre} (${producto.cantidad} u.)\n")
                    }
                    textoResultado.text = stringBuilder.toString()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}