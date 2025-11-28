package com.example.evaluacion2_petsonline.domain.model

data class Reserva(
    val id: Int,
    val nombreMascota: String,
    val servicio: String,
    val fecha: String,
    val observacion: String
)