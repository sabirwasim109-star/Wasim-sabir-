package com.example.data.repository

import com.example.data.api.GeminiPlantService
import com.example.data.db.CareLogEntity
import com.example.data.db.PlantDao
import com.example.data.db.PlantEntity
import com.example.data.model.PlantCareInfo
import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao) {

    val allSavedPlants: Flow<List<PlantEntity>> = plantDao.getAllPlants()

    fun getPlantById(id: Int): Flow<PlantEntity?> = plantDao.getPlantById(id)

    fun getCareLogsForPlant(id: Int): Flow<List<CareLogEntity>> = plantDao.getCareLogsForPlant(id)

    suspend fun savePlant(plant: PlantEntity): Long = plantDao.insertPlant(plant)

    suspend fun updatePlant(plant: PlantEntity) = plantDao.updatePlant(plant)

    suspend fun deletePlant(plant: PlantEntity) = plantDao.deletePlant(plant)

    suspend fun markWatered(plantId: Int) {
        val now = System.currentTimeMillis()
        plantDao.markWatered(plantId, now)
        plantDao.insertCareLog(
            CareLogEntity(
                plantId = plantId,
                careType = "WATER",
                timestamp = now,
                note = "Watered plant thoroughly"
            )
        )
    }

    suspend fun addCareLog(log: CareLogEntity) {
        plantDao.insertCareLog(log)
    }

    suspend fun identifyPlantFromPhoto(bitmap: android.graphics.Bitmap): Result<PlantCareInfo> {
        return GeminiPlantService.identifyPlantFromPhoto(bitmap)
    }

    suspend fun chatWithPlantDoctor(
        prompt: String,
        history: List<Pair<String, String>>,
        bitmap: android.graphics.Bitmap? = null
    ): Result<String> {
        return GeminiPlantService.chatWithPlantDoctor(prompt, history, bitmap)
    }
}
