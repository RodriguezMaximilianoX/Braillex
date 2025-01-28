package com.rmxdev.braillex.domain.entities

import com.google.gson.annotations.SerializedName

data class PdfFile(
    val id: String,
    val title: String,
    @SerializedName("audioUrl") val audioUrl: String
)