package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_plants")
data class PlantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val botanicalName: String,
    val location: String = "Indoor",
    val imageUri: String? = null,
    val lightNeeds: String = "Bright indirect light",
    val wateringDaysInterval: Int = 7,
    val lastWateredTimestamp: Long = System.currentTimeMillis(),
    val healthStatus: String = "Healthy",
    val notes: String = "",
    val summary: String = "",
    val soilPreference: String = "Well-draining potting soil",
    val toxicity: String = "Non-toxic",
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "care_logs")
data class CareLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plantId: Int,
    val careType: String, // "WATER", "FERTILIZE", "PRUNE", "REPOT"
    val timestamp: Long = System.currentTimeMillis(),
    val note: String? = null
)
