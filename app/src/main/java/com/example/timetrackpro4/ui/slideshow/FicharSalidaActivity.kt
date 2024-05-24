package com.example.timetrackpro4.ui.slideshow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.timetrackpro4.R
import com.example.timetrackpro4.ui.slideshow.FicharEntradaActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class FicharSalidaActivity : AppCompatActivity() {

    // Referencia a los elementos de la interfaz
    private lateinit var btnSalida: ImageButton
    private lateinit var tvTiempoTranscurrido: TextView

    // Instancias de Firebase
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUser = firebaseAuth.currentUser
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Variable para el informe de fichajes
    private val reporteFichajes = mutableListOf<Pair<String, String>>()

    // Tiempo en milisegundos del último fichaje de entrada
    private var ultimoFichaje: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fichar_salida)

        // Inicializar las referencias a los elementos de la interfaz
        btnSalida = findViewById(R.id.btnSalida)
        tvTiempoTranscurrido = findViewById(R.id.tvTiempoTranscurrido)

        // Obtener el tiempo del último fichaje de entrada desde la actividad anterior
        ultimoFichaje = intent.getLongExtra("ultimoFichaje", 0)

        // Establecer el listener para el botón de salida
        btnSalida.setOnClickListener {
            ficharSalida()
        }

        // Iniciar la actualización del contador de tiempo
        startTimeUpdater()
    }

    private fun startTimeUpdater() {
        val timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val tiempoTranscurrido = System.currentTimeMillis() - ultimoFichaje
                updateTimeDisplay(tiempoTranscurrido)
            }

            override fun onFinish() {}
        }
        timer.start()
    }

    private fun updateTimeDisplay(tiempoTranscurrido: Long) {
        val horas = (tiempoTranscurrido / (1000 * 60 * 60)).toInt()
        val minutos = ((tiempoTranscurrido % (1000 * 60 * 60)) / (1000 * 60)).toInt()
        val segundos = ((tiempoTranscurrido % (1000 * 60)) / 1000).toInt()

        tvTiempoTranscurrido.text = String.format("%02d:%02d:%02d", horas, minutos, segundos)
    }

    // Registrar la salida del usuario en Firestore
    private fun ficharSalida() {
        Log.d("FicharSalidaActivity", "Método ficharSalida() llamado")

        // Obtener la hora actual
        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        // Agregar el fichaje de salida al informe
        reporteFichajes.add(Pair("Salida", horaActual))

        // Registrar la salida en Firestore
        val currentDate = Calendar.getInstance().time
        val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(currentDate)

        val salidaData = hashMapOf(
            "userId" to currentUser?.uid,
            "fechaSalida" to formattedDate
        )

        firestore.collection("fichajes")
            .add(salidaData)
            .addOnSuccessListener {
                // Mostrar un mensaje y redirigir al usuario a la actividad de fichar entrada
                Log.d("FicharSalidaActivity", "Salida registrada a las $horaActual")
                Toast.makeText(this, "Salida registrada a las $horaActual", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, FicharEntradaActivity::class.java)
                intent.putExtra("ultimoFichaje", System.currentTimeMillis()) // Actualizar el tiempo del último fichaje de entrada
                startActivity(intent)
                finish() // Cerrar la actividad actual para que el usuario no pueda volver atrás
            }
            .addOnFailureListener { e ->
                // Mostrar un mensaje de error si hay problemas al registrar la salida
                Log.d("FicharSalidaActivity", "Error al registrar la salida: $e")
                Toast.makeText(this, "Error al registrar la salida: $e", Toast.LENGTH_SHORT).show()
            }
    }
}
