package com.example.timetrackpro4

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.timetrackpro4.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var btnMenu: ImageButton
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el diseño de la actividad principal
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar la barra de acción y el menú de navegación
        setSupportActionBar(binding.appBarMain.toolbar)

        // Configurar el diseño del cajón de navegación
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Obtener el controlador de navegación
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Configurar la AppBarConfiguration para la navegación
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        // Configurar la acción de la barra de acción con el controlador de navegación
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Configurar el menú de navegación con el controlador de navegación
        navView.setupWithNavController(navController)

        // Obtener referencias a los elementos de la barra de aplicación
        btnMenu = binding.appBarMain.btnMenu
        tvTitle = binding.appBarMain.tvTitle

        // Configurar la acción del botón de menú
        btnMenu.setOnClickListener {
            // Aquí agrega la lógica para abrir el menú lateral
            drawerLayout.open()
        }

        // Establecer el título de la aplicación
        tvTitle.text = getString(R.string.app_name)
    }

    // Implementar la lógica de navegación de la barra de acción
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
