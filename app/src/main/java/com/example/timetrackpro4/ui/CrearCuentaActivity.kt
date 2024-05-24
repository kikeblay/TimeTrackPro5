package com.example.timetrackpro4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class CrearCuentaActivity : AppCompatActivity() {
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnCrearCuenta: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        // Inicializar las variables de clase con los elementos de la vista
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)

        firebaseAuth = FirebaseAuth.getInstance()

        btnCrearCuenta.setOnClickListener {
            crearCuentaUsuario()
        }
    }

    private fun crearCuentaUsuario() {
        val correo = etCorreo.text.toString()
        val contrasena = etContrasena.text.toString()

        // Validar que los campos no estén vacíos
        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo electrónico y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Cuenta de usuario creada exitosamente, redirigir al usuario a la pantalla de inicio de sesión
                    Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // Manejar los errores de creación de cuenta
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthException) {
                        Toast.makeText(this, "Error al crear la cuenta: ${e.message}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error desconocido: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
