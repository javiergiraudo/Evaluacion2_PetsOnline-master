package com.example.evaluacion2_petsonline.domain.model

data class Mascota(
    val id: Int,
    val nombre: String,
    val especie: String,
    val edad: Int,
    val descripcion: String
)