package com.example.model

data class LongTermService(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val durationMonths: Int,
    val priceEgp: Double,
    val featuresAr: List<String>,
    val featuresEn: List<String>,
    val isPopular: Boolean = false,
    val isAvailable: Boolean = true
)
