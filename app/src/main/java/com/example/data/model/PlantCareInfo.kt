package com.example.data.model

data class PlantCareInfo(
    val commonName: String = "",
    val botanicalName: String = "",
    val summary: String = "",
    val confidence: Int = 90,
    val healthStatus: String = "Healthy",
    val lightNeeds: String = "Bright, indirect light",
    val waterSchedule: String = "Water every 7 days",
    val wateringDaysInterval: Int = 7,
    val humidityAndTemp: String = "65-80°F (18-27°C)",
    val soilPreference: String = "Well-draining potting soil",
    val fertilizerInfo: String = "Feed monthly in spring & summer",
    val toxicity: String = "Non-toxic to pets",
    val pruningTips: String = "Trim dead leaves periodically",
    val commonIssues: String = "Avoid soggy roots to prevent root rot",
    val stepByStepCare: List<String> = emptyList()
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: ChatSender,
    val text: String,
    val imageBase64: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class ChatSender {
    USER, BOT
}
