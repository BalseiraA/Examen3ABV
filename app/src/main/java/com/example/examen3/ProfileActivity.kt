package com.example.examen3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.examen3.databinding.ActivityProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USUARIO_ID = "EXTRA_USUARIO_ID"
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCerrarPerfil.setOnClickListener {
            finish()
        }

        cargarPerfil()
    }

    private fun cargarPerfil() {
        val usuarioId = intent.getIntExtra(EXTRA_USUARIO_ID, -1)

        if (usuarioId == -1) {
            Toast.makeText(this, "No se recibió el jugador", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val usuarioDao = MyApplication.getDatabase(this).usuarioDao()

        lifecycleScope.launch {
            val usuario = withContext(Dispatchers.IO) {
                usuarioDao.obtenerUsuarioPorId(usuarioId)
            }

            if (usuario == null) {
                Toast.makeText(
                    this@ProfileActivity,
                    "No se encontró el jugador",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
                return@launch
            }

            val ultimaConexionAnterior = usuario.ultimaConexion ?: "Primera conexión registrada"

            binding.tvwNombreJugador.text = "Jugador: ${usuario.nombre}"
            binding.tvwUltimaConexion.text = "Última conexión anterior: $ultimaConexionAnterior"

            val fechaActual = obtenerFechaActual()

            withContext(Dispatchers.IO) {
                usuarioDao.actualizarUltimaConexion(usuario.id, fechaActual)
            }
        }
    }

    private fun obtenerFechaActual(): String {
        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return formato.format(Date())
    }
}