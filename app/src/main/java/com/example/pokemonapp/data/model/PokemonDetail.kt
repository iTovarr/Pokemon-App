package com.example.pokemonapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("types") val types: List<TypeSlot>,
    @SerializedName("stats") val stats: List<StatSlot>
)

data class TypeSlot(
    @SerializedName("type") val type: TypeInfo
)

data class TypeInfo(
    @SerializedName("name") val name: String
)

data class StatSlot(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("stat") val stat: StatInfo
)

data class StatInfo(
    @SerializedName("name") val name: String
)