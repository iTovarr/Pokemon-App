package com.example.pokemonapp

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.pokemonapp.ui.detail.DetailScreen
import com.example.pokemonapp.ui.home.HomeScreen
import com.example.pokemonapp.ui.home.HomeViewModel
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private val homeViewModel: HomeViewModel by viewModels()
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el motor de Texto a Voz
        tts = TextToSpeech(this, this)

        setContent {
            var selectedPokemonId by remember { mutableStateOf<Int?>(null) }

            if (selectedPokemonId == null) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onPokemonClick = { id -> selectedPokemonId = id }
                )
            } else {
                DetailScreen(
                    pokemonId = selectedPokemonId!!,
                    onBackClick = {
                        tts?.stop() // Detiene la voz si el usuario regresa
                        selectedPokemonId = null
                    },
                    onSpeak = { texto -> speak(texto) } // Pasamos la función de hablar
                )
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale("es", "ES") // Configurado en español
        }
    }

    private fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onDestroy()
    }
}