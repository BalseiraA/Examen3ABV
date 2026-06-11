package com.example.examen3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.examen3.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnIniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        binding.btnRegistrar.setOnClickListener {
            validarRegistroExistente()
        }
    }

    private fun iniciarSesion() {
        val nombre = binding.edtLoginUsuario.text.toString().trim()
        val password = binding.edtLoginPassword.text.toString().trim()

        if (nombre.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Escribe usuario y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        val usuarioDao = MyApplication.getDatabase(this).usuarioDao()

        lifecycleScope.launch {
            val usuario = withContext(Dispatchers.IO) {
                usuarioDao.login(nombre, password)
            }

            if (usuario == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Las credenciales no son correctas",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                intent.putExtra(ProfileActivity.EXTRA_USUARIO_ID, usuario.id)
                startActivity(intent)
            }
        }
    }

    private fun validarRegistroExistente() {
        val usuarioDao = MyApplication.getDatabase(this).usuarioDao()

        lifecycleScope.launch {
            val cantidadUsuarios = withContext(Dispatchers.IO) {
                usuarioDao.contarUsuarios()
            }

            if (cantidadUsuarios > 0) {
                Toast.makeText(
                    this@MainActivity,
                    "Ya está registrado",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this@MainActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}