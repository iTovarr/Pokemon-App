package com.example.pokemonapp.ui.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokemonapp.data.model.PokemonNamedResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            // Cabecera roja de la Pokédex con sus luces características del anime
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Animación infinita de brillo para el lente principal
                        val infiniteTransition = rememberInfiniteTransition(label = "lente")
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0.4f,
                            targetValue = 1.0f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "brillo"
                        )

                        // El lente de la Pokédex latiendo con luz azul brillante
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF2196F3).copy(alpha = alpha), CircleShape)
                                .border(3.dp, Color.White, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Luces parpadeantes pequeñas (Anidadas en fila al costado)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.size(10.dp).background(Color(0xFFFF2D55), CircleShape))
                            Box(modifier = Modifier.size(10.dp).background(Color(0xFFFFCC00), CircleShape))
                            Box(modifier = Modifier.size(10.dp).background(Color(0xFF4CD964), CircleShape))
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Text("POKÉDEX KANTO", fontWeight = FontWeight.Black, color = Color.White, fontSize = 18.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDC0A2D)) // Rojo Pokédex
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF8B0000)) // Fondo rojo oscuro para la estructura física
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
                }
                is HomeUiState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.pokemon) { poke ->
                            AnimePokemonCard(pokemon = poke, onClick = { onPokemonClick(poke.id) })
                        }
                    }
                }
                is HomeUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.message, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.fetchPokemon() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimePokemonCard(pokemon: PokemonNamedResult, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 24.dp, bottomStart = 24.dp, bottomEnd = 0.dp), // Corte asimétrico retro
        colors = CardDefaults.cardColors(containerColor = Color(0xFF232323)), // Pantalla gris oscuro estilo LCD viejo
        modifier = Modifier
            .fillMaxWidth()
            .border(3.dp, Color(0xFFDEDEDE), RoundedCornerShape(topStart = 0.dp, topEnd = 24.dp, bottomStart = 24.dp, bottomEnd = 0.dp))
            .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            // Número estilo pantalla digital verde neón
            Text(
                text = String.format("Nº %03d", pokemon.id),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF51FF00),
                modifier = Modifier.align(Alignment.Start)
            )

            // El recuadro blanco de la ilustración interna
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color(0xFFEFEFEF), RoundedCornerShape(12.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = pokemon.name.uppercase(),
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}