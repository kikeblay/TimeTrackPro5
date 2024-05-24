package com.example.timetrackpro4.ui.slideshow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.timetrackpro4.R
import com.example.timetrackpro4.ui.slideshow.FicharSalidaActivity
import java.text.SimpleDateFormat
import java.util.*

class FicharEntradaActivity : AppCompatActivity() {

    // Referencia a los elementos de la interfaz
    private lateinit var tvEstado: TextView
    private lateinit var btnEntrada: ImageButton
    private lateinit var tvFecha: TextView
    private lateinit var tvHora: TextView

    // Variable para el informe de fichajes
    private val reporteFichajes = mutableListOf<Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fichar_entrada)

        // Inicializar las referencias a los elementos de la interfaz
        tvEstado = findViewById(R.id.tvEstado)
        btnEntrada = findViewById(R.id.btnEntrada)
        tvFecha = findViewById(R.id.tvFecha)
        tvHora = findViewById(R.id.tvHora)

        // Establecer el listener para el botón de entrada
        btnEntrada.setOnClickListener {
            ficharEntrada()
        }

        // Actualizar la fecha y hora iniciales
        actualizarFechaHora()
    }

    private fun ficharEntrada() {
        // Obtener la hora actual
        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        // Agregar el fichaje de entrada al informe
        reporteFichajes.add(Pair("Entrada", horaActual))

        // Mostrar un mensaje o realizar otras acciones necesarias
        Toast.makeText(this, "Entrada registrada a las $horaActual", Toast.LENGTH_SHORT).show()

        // Redirigir al usuario a la actividad de fichar salida
        val intent = Intent(this, FicharSalidaActivity::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual para que el usuario no pueda volver atrás
    }

    private fun actualizarFechaHora() {
        // Obtener la fecha y hora actual
        val fechaActual = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale.getDefault()).format(Date())
        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        // Actualizar los TextView correspondientes
        tvFecha.text = fechaActual
        tvHora.text = horaActual
    }
}
