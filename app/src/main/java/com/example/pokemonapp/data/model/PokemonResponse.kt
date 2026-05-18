package com.example.pokemonapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("results") val results: List<PokemonNamedResult>
)

data class PokemonNamedResult(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) {
    val id: Int
        get() {
            val tokens = url.split("/")
            return tokens[tokens.size - 2].toIntOrNull() ?: 1
        }

    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}