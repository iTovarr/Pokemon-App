package com.example.pokemonapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    // Este estado controla qué imagen se proyecta en el cuadro grande de arriba
    var imagenSeleccionadaUrl by remember { mutableStateOf("") }

    val esSalvaje = pokemonId % 3 == 0

    LaunchedEffect(pokemonId) {
        try {
            isLoading = true
            val response = RetrofitClient.apiService.getPokemonDetail(pokemonId)
            detailState = response

            // Por defecto, la pantalla grande muestra el Official Artwork HD
            imagenSeleccionadaUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${response.id}.png"
            isLoading = false

            val primerTipo = response.types.firstOrNull()?.type?.name?.lowercase() ?: "normal"

            val habitatNombre = when (primerTipo) {
                "grass", "bug" -> "BOSQUE Y PRADERA"
                "fire", "ground", "rock" -> "MONTAÑA ROCOSA"
                "water", "ice" -> "MAR ABIERTO"
                "electric", "psychic" -> "ZONA URBANA"
                else -> "PRADERA ABIERTA"
            }

            val entrenadorNombre = when (primerTipo) {
                "grass", "electric" -> "ASH KETCHUM"
                "fire", "dark" -> "GARY OAK"
                "rock", "ground" -> "BROCK"
                "water" -> "MISTY"
                else -> "SALVAJE"
            }

            val frasePokemon = if (entrenadorNombre == "SALVAJE") {
                "${response.name}. Detectado en estado salvaje en el hábitat ${habitatNombre}."
            } else {
                "${response.name}. Registrado bajo el control del entrenador ${entrenadorNombre} en el entorno de ${habitatNombre}."
            }
            onSpeak(frasePokemon)

        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Error de sincronización"
            isLoading = false
        }
    }

    val chasisRojoFondo = Color(0xFFC92A2A)
    val fondoGrisFocal = Color(0xFFF3F4F6)
    val verdeContenedorEtiqueta = Color(0xFF708F58)
    val circuloContrasteOscuro = Color(0xFF2E2E2E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("REGISTRO AVANZADO DE CAMPO", fontWeight = FontWeight.Black, color = Color.White, fontSize = 15.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = chasisRojoFondo)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(chasisRojoFondo)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
            } else if (errorMessage != null) {
                Text(errorMessage!!, color = Color.White, modifier = Modifier.align(Alignment.Center))
            } else {
                val detail = detailState!!

                // Definimos los tres sprites fijos de la PokéAPI
                val urlFrente = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${detail.id}.png"
                val urlEspaldas = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${detail.id}.png"
                val urlShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/${detail.id}.png"

                val tipoPrincipal = detail.types.firstOrNull()?.type?.name?.lowercase() ?: "normal"

                // Mapeo estable de las ilustraciones complementarias
                val habitatData = when (tipoPrincipal) {
                    "grass", "bug" -> Pair("BOSQUE Y PRADERA NATAL", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/honey.png")
                    "fire", "rock", "ground" -> Pair("MONTAÑA ROCOSA VOLCÁNICA", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/hard-stone.png")
                    "water", "ice" -> Pair("OCÉANO Y MAR ABIERTO", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/water-stone.png")
                    "electric", "psychic", "steel" -> Pair("ZONA URBANA CENTRAL", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/magnet.png")
                    else -> Pair("PRADERA ABIERTA TRADICIONAL", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/soft-sand.png")
                }

                val dietaData = when (tipoPrincipal) {
                    "grass", "bug", "normal" -> Pair("BAYA ARANJA SILVESTRE", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/oran-berry.png")
                    "fire", "electric" -> Pair("BAYA FRAMBU PICANTE", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/razz-berry.png")
                    "water", "ice" -> Pair("BAYA LATANO REFRESCANTE", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/nanab-berry.png")
                    else -> Pair("BAYA ZIDRA CURATIVA", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/sitrus-berry.png")
                }

                val entrenadorData = when (tipoPrincipal) {
                    "grass", "electric" -> Pair("ASH KETCHUM (LIGA KANTO)", "https://api.dicebear.com/7.x/identicon/png?seed=Ash&backgroundColor=ffd43b")
                    "fire", "poison", "dark" -> Pair("GARY OAK (RIVAL REGIONAL)", "https://api.dicebear.com/7.x/identicon/png?seed=Gary&backgroundColor=4dabf7")
                    "rock", "ground", "fighting" -> Pair("BROCK (LÍDER DE GIMNASIO)", "https://api.dicebear.com/7.x/identicon/png?seed=Brock&backgroundColor=a9e34b")
                    "water" -> Pair("MISTY (LÍDER CELESTE)", "https://api.dicebear.com/7.x/identicon/png?seed=Misty&backgroundColor=faa2c1")
                    else -> Pair("NINGUNO (ESTADO SALVAJE)", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/radar.png")
                }

                val urlEstadoImagen = if (entrenadorData.first.contains("SALVAJE")) {
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/escape-rope.png"
                } else {
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png"
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // =================== 1. MONITOR PRINCIPAL DE IMAGEN (GRANDE) ===================
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = fondoGrisFocal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(24.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = detail.name.uppercase(),
                                fontWeight = FontWeight.Black,
                                color = verdeContenedorEtiqueta,
                                fontSize = 18.sp,
                                modifier = Modifier.align(Alignment.TopStart)
                            )

                            Text(
                                text = "ID: #${String.format("%03d", detail.id)}",
                                fontWeight = FontWeight.Black,
                                color = verdeContenedorEtiqueta,
                                fontSize = 14.sp,
                                modifier = Modifier.align(Alignment.TopEnd)
                            )

                            AsyncImage(
                                model = imagenSeleccionadaUrl,
                                contentDescription = detail.name,
                                modifier = Modifier.size(150.dp).padding(top = 16.dp),
                                contentScale = ContentScale.Fit,
                                filterQuality = FilterQuality.None
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // =================== 2. HILERA DE 3 RECUADROS MINIATURA (COMO EN TU IMAGEN) ===================
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Miniatura 1: Frente
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .background(fondoGrisFocal, RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                                    .clickable { imagenSeleccionadaUrl = urlFrente },
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(model = urlFrente, contentDescription = "Frente", modifier = Modifier.size(80.dp), filterQuality = FilterQuality.None)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Frente", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                        }

                        // Miniatura 2: Espaldas
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .background(fondoGrisFocal, RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                                    .clickable { imagenSeleccionadaUrl = urlEspaldas },
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(model = urlEspaldas, contentDescription = "Espaldas", modifier = Modifier.size(80.dp), filterQuality = FilterQuality.None)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Espalda", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                        }

                        // Miniatura 3: Shiny
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .background(fondoGrisFocal, RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                                    .clickable { imagenSeleccionadaUrl = urlShiny },
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(model = urlShiny, contentDescription = "Shiny", modifier = Modifier.size(80.dp), filterQuality = FilterQuality.None)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Shiny", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // =================== 3. TARJETAS COMPLEMENTARIAS FILTRADAS POR TIPO ===================
                    Text(
                        text = "DETALLES COMPLEMENTARIOS DE CAMPO",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    // Recuadro 1: Datos del Entrenador
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = fondoGrisFocal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(model = entrenadorData.second, contentDescription = null, modifier = Modifier.size(45.dp).clip(CircleShape).background(circuloContrasteOscuro).padding(6.dp), contentScale = ContentScale.Fit)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("REGISTRO DE ENTRENADOR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = verdeContenedorEtiqueta)
                                Text(entrenadorData.first, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.DarkGray)
                            }
                        }
                    }

                    // Recuadro 2: Datos del Hábitat
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = fondoGrisFocal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(model = habitatData.second, contentDescription = null, modifier = Modifier.size(45.dp).clip(RoundedCornerShape(8.dp)).background(circuloContrasteOscuro).padding(6.dp), contentScale = ContentScale.Fit)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("ENTORNO / HÁBITAT ASIGNADO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = verdeContenedorEtiqueta)
                                Text(habitatData.first, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.DarkGray)
                            }
                        }
                    }

                    // Recuadro 3: Datos de la Dieta
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = fondoGrisFocal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(model = dietaData.second, contentDescription = null, modifier = Modifier.size(45.dp).clip(RoundedCornerShape(8.dp)).background(circuloContrasteOscuro).padding(4.dp), contentScale = ContentScale.Fit)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text("DIETA NUTRICIONAL RECOMENDADA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = verdeContenedorEtiqueta)
                                Text(dietaData.first, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.DarkGray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // =================== 4. CUADRÍCULA ESTILO POKÉMON GO (ESTADO Y TIPO) ===================
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1.3f)) {
                            Text(text = "ESTADO ACTUAL", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.White,
                                modifier = Modifier.fillMaxWidth().background(verdeContenedorEtiqueta, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)).padding(horizontal = 8.dp, vertical = 4.dp))
                            Row(modifier = Modifier.fillMaxWidth().background(fondoGrisFocal, RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)).padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                AsyncImage(model = urlEstadoImagen, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (entrenadorData.first.contains("SALVAJE")) "SALVAJE" else "CAPTURADO", fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                        }

                        Column(modifier = Modifier.weight(0.9f)) {
                            Text(text = "TIPO POKÉMON", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.White,
                                modifier = Modifier.fillMaxWidth().background(verdeContenedorEtiqueta, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)).padding(horizontal = 8.dp, vertical = 4.dp))
                            Row(modifier = Modifier.fillMaxWidth().height(41.dp).background(fondoGrisFocal, RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                detail.types.forEach { slot ->
                                    Text(text = slot.type.name.uppercase() + " ", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.DarkGray)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // =================== 5. ESTADÍSTICAS AVANZADAS DEL SISTEMA ===================
                    detail.stats.forEach { statSlot ->
                        val progreso = statSlot.baseStat / 150f
                        val cleanName = when (statSlot.stat.name) {
                            "hp" -> "HP"
                            "attack" -> "ATQ"
                            "defense" -> "DEF"
                            "special-attack" -> "SATQ"
                            "special-defense" -> "SDEF"
                            "speed" -> "VEL"
                            else -> statSlot.stat.name.uppercase()
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = cleanName, modifier = Modifier.weight(0.15f), fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.Gray)
                            LinearProgressIndicator(progress = { progreso.coerceAtMost(1f) }, modifier = Modifier.weight(0.55f).height(14.dp).clip(RoundedCornerShape(4.dp)), color = verdeContenedorEtiqueta, trackColor = Color(0xFFE5E7EB))
                            Text(text = "${cleanName}: ${statSlot.baseStat.toString().padStart(3, '0')}", modifier = Modifier.weight(0.3f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.DarkGray, textAlign = TextAlign.End)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}