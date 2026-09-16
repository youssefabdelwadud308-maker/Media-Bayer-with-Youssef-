package com.example.model

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val isFromAiOrSupport: Boolean,
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis()
)
