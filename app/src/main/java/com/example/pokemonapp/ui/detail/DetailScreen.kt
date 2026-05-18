package com.example.pokemonapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.remote.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var detailState by remember { mutableStateOf<PokemonDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pokemonId) {
        try {
            isLoading = true
            val response = RetrofitClient.apiService.getPokemonDetail(pokemonId)
            detailState = response
            isLoading = false

            // Acción del anime: Cuando los datos cargan con éxito, la Pokédex narra la ficha técnica
            val primerTipo = response.types.firstOrNull()?.type?.name ?: "Desconocido"
            val frasePokemon = "${response.name}. Pokémon de tipo $primerTipo. Tiene un peso de ${response.weight / 10.0} kilos y una altura de ${response.height / 10.0} metros. Analizando estadísticas base."
            onSpeak(frasePokemon)

        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Error del sistema Pokédex"
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ANÁLISIS DE DATOS", fontWeight = FontWeight.Black, color = Color.White, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDC0A2D))
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFDC0A2D)) // Fondo rojo Pokédex original del anime
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
            } else if (errorMessage != null) {
                Text(errorMessage!!, color = Color.White, modifier = Modifier.align(Alignment.Center))
            } else {
                val detail = detailState!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Contenedor de Pantalla de Escaneo Física (Gris con detalles mecánicos)
                    Card(
                        shape = RoundedCornerShape(bottomStart = 32.dp, topEnd = 12.dp, topStart = 12.dp, bottomEnd = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDEDEDE)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .border(4.dp, Color(0xFFB0B0B0), RoundedCornerShape(bottomStart = 32.dp, topEnd = 12.dp, topStart = 12.dp, bottomEnd = 12.dp))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            // Luces LED pequeñas del chasis
                            Row(modifier = Modifier.padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(modifier = Modifier.size(8.dp).background(Color.Red, CircleShape))
                                Box(modifier = Modifier.size(8.dp).background(Color.Red, CircleShape))
                            }

                            // Pantalla de visualización (Fondo de cristal oscuro de laboratorio)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .background(Color(0xFF303030), RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${detail.id}.png",
                                    contentDescription = detail.name,
                                    modifier = Modifier.size(160.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            // Botón físico redondo y rendijas de ventilación/altavoz
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(14.dp).background(Color.Red, CircleShape))
                                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    repeat(4) { Box(modifier = Modifier.size(width = 15.dp, height = 3.dp).background(Color.Black)) }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nombre del Pokémon en mayúsculas de terminal informática
                    Text(
                        text = detail.name.uppercase(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tipos de elementos estilo chips/cápsulas físicas
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        detail.types.forEach { slot ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF424242))
                                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = slot.type.name.uppercase(),
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Panel de Control Verde (Simula la subpantalla de terminal fósforo del anime)
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF51FF00).copy(alpha = 0.15f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color(0xFF51FF00), RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("ALTURA: ${detail.height / 10.0} m", color = Color(0xFF51FF00), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("PESO: ${detail.weight / 10.0} kg", color = Color(0xFF51FF00), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color(0xFF51FF00).copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("ESTADÍSTICAS:", color = Color(0xFF51FF00), fontWeight = FontWeight.Black, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            detail.stats.forEach { statSlot ->
                                AnimeStatRow(name = statSlot.stat.name, value = statSlot.baseStat)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Botones mecánicos decorativos de colores del juguete de Kanto
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                        Box(modifier = Modifier.size(width = 45.dp, height = 35.dp).background(Color(0xFF2196F3), RoundedCornerShape(4.dp)).border(1.dp, Color.Black, RoundedCornerShape(4.dp)))
                        Box(modifier = Modifier.size(width = 45.dp, height = 35.dp).background(Color(0xFF4CAF50), RoundedCornerShape(4.dp)).border(1.dp, Color.Black, RoundedCornerShape(4.dp)))
                        Box(modifier = Modifier.size(width = 45.dp, height = 35.dp).background(Color(0xFFFFCC00), RoundedCornerShape(4.dp)).border(1.dp, Color.Black, RoundedCornerShape(4.dp)))
                    }
                }
            }
        }
    }
}

@Composable
fun AnimeStatRow(name: String, value: Int) {
    val progress = value / 150f
    val cleanName = when(name) {
        "hp" -> "HP"
        "attack" -> "ATQ"
        "defense" -> "DEF"
        "special-attack" -> "AT.ESP"
        "special-defense" -> "DEF.ESP"
        "speed" -> "VEL"
        else -> name.uppercase()
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = cleanName, modifier = Modifier.weight(0.25f), color = Color(0xFF51FF00), fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(text = value.toString().padStart(3, '0'), modifier = Modifier.weight(0.15f), color = Color(0xFF51FF00), fontWeight = FontWeight.Black, fontSize = 12.sp)

        LinearProgressIndicator(
            progress = { progress.coerceAtMost(1f) },
            modifier = Modifier.weight(0.6f).height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = Color(0xFF51FF00),
            trackColor = Color(0xFF51FF00).copy(alpha = 0.15f)
        )
    }
}