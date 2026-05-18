package com.example.pokemonapp.ui.theme

import androidx.compose.ui.graphics.Color

fun getPokemonColor(type: String?): Color {
    return companionColors[type?.lowercase()] ?: Color(0xFFF2F2F2)
}

private val companionColors = mapOf(
    "grass" to Color(0xFFD2F1D2),
    "fire" to Color(0xFFFCD9C4),
    "water" to Color(0xFFD4E6FA),
    "bug" to Color(0xFFEAF0C2),
    "normal" to Color(0xFFEBEBE1),
    "poison" to Color(0xFFF3DDF3),
    "electric" to Color(0xFFFEF6C9),
    "ground" to Color(0xFFF7ECDF),
    "fairy" to Color(0xFFFCE6ED),
    "fighting" to Color(0xFFF5D6D6),
    "psychic" to Color(0xFFFCD7E5),
    "rock" to Color(0xFFEBE3CE),
    "ghost" to Color(0xFFE0D8EB),
    "ice" to Color(0xFFE4F5F6),
    "dragon" to Color(0xFFE3DCFC)
)