package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Query("SELECT * FROM saved_plants ORDER BY createdTimestamp DESC")
    fun getAllPlants(): Flow<List<PlantEntity>>

    @Query("SELECT * FROM saved_plants WHERE id = :id LIMIT 1")
    fun getPlantById(id: Int): Flow<PlantEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: PlantEntity): Long

    @Update
    suspend fun updatePlant(plant: PlantEntity)

    @Delete
    suspend fun deletePlant(plant: PlantEntity)

    @Query("UPDATE saved_plants SET lastWateredTimestamp = :timestamp WHERE id = :plantId")
    suspend fun markWatered(plantId: Int, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM care_logs WHERE plantId = :plantId ORDER BY timestamp DESC")
    fun getCareLogsForPlant(plantId: Int): Flow<List<CareLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareLog(log: CareLogEntity)
}
