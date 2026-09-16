package com.example.model

data class PlatformIntegrationConfig(
    val id: String,
    val platformKey: String,
    val nameAr: String,
    val nameEn: String,
    val isEnabled: Boolean = true,
    val apiStatus: String = "Connected",
    val addedByGm: Boolean = false
)

data class DeveloperPromptAction(
    val id: String,
    val requestPrompt: String,
    val responseSummary: String,
    val executedActionType: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class FaqItem(
    val id: String,
    val questionAr: String,
    val questionEn: String,
    val answerAr: String,
    val answerEn: String,
    val category: String
)
