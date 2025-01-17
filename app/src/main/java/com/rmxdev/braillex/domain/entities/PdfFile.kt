package com.rmxdev.braillex.domain.entities

data class PdfFile(
    val id: String,
    val title: String,
    val audioUrl: String,
    val qrCodeUrl: String
)