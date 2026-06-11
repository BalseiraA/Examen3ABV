package com.example.examen3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.examen3.databinding.ActivitySignUpBinding
import com.example.examen3.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRegister.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val nombre = binding.edtRegisterUser.text.toString().trim()
        val password = binding.edtRegisterPassword.text.toString().trim()

        if (nombre.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Completa usuario y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        val usuarioDao = MyApplication.getDatabase(this).usuarioDao()

        lifecycleScope.launch {
            val yaExisteNombre = withContext(Dispatchers.IO) {
                usuarioDao.obtenerUsuarioPorNombre(nombre) != null
            }

            if (yaExisteNombre) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Ese usuario ya existe",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

            withContext(Dispatchers.IO) {
                usuarioDao.insertarUsuario(
                    Usuario(
                        nombre = nombre,
                        password = password
                    )
                )
            }

            Toast.makeText(
                this@SignUpActivity,
                "Jugador registrado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}