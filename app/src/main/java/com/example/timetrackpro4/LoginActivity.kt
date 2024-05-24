package com.example.timetrackpro4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.timetrackpro4.ui.slideshow.FicharEntradaActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnCrearCuenta: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar las variables de clase con los elementos de la vista
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)

        firebaseAuth = FirebaseAuth.getInstance()

        // Verificar si el usuario ya ha iniciado sesión
        if (firebaseAuth.currentUser != null) {
            // Redirigir al usuario a la pantalla de fichaje de entrada
            val intent = Intent(this, FicharEntradaActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRegistrar.setOnClickListener {
            loginUsuario()
        }

        btnCrearCuenta.setOnClickListener {
            val intent = Intent(this, CrearCuentaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUsuario() {
        val correo = etCorreo.text.toString()
        val contrasena = etContrasena.text.toString()

        // Validar que los campos no estén vacíos
        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo electrónico y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, redirigir a la siguiente pantalla
                    val intent = Intent(this, FicharEntradaActivity::class.java)
                    startActivity(intent)
                    finish() // Finalizar esta actividad para que el usuario no pueda volver atrás
                } else {
                    // Manejar los errores de inicio de sesión
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error de inicio de sesión: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
