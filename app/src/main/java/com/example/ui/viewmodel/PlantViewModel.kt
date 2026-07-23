package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.CareLogEntity
import com.example.data.db.PlantEntity
import com.example.data.model.ChatMessage
import com.example.data.model.ChatSender
import com.example.data.model.PlantCareInfo
import com.example.data.repository.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface PlantIdentificationUiState {
    object Idle : PlantIdentificationUiState
    object Loading : PlantIdentificationUiState
    data class Success(val careInfo: PlantCareInfo, val photoBitmap: Bitmap? = null) : PlantIdentificationUiState
    data class Error(val message: String) : PlantIdentificationUiState
}

class PlantViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlantRepository
    val savedPlants: StateFlow<List<PlantEntity>>

    private val _identificationState = MutableStateFlow<PlantIdentificationUiState>(PlantIdentificationUiState.Idle)
    val identificationState: StateFlow<PlantIdentificationUiState> = _identificationState.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = ChatSender.BOT,
                text = "Hello! I am Dr. Flora, your AI Plant Doctor & Botanist. How can I help your garden flourish today? Ask me about yellow leaves, pest control, watering, or attach a photo of your plant!"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatGenerating = MutableStateFlow(false)
    val isChatGenerating: StateFlow<Boolean> = _isChatGenerating.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = PlantRepository(db.plantDao())
        savedPlants = repository.allSavedPlants.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun identifyPlant(bitmap: Bitmap) {
        viewModelScope.launch {
            _identificationState.value = PlantIdentificationUiState.Loading
            val result = repository.identifyPlantFromPhoto(bitmap)
            result.onSuccess { info ->
                _identificationState.value = PlantIdentificationUiState.Success(info, bitmap)
            }.onFailure { err ->
                _identificationState.value = PlantIdentificationUiState.Error(err.localizedMessage ?: "Plant identification failed. Please try again.")
            }
        }
    }

    fun loadSamplePlant(sampleInfo: PlantCareInfo) {
        _identificationState.value = PlantIdentificationUiState.Success(sampleInfo, null)
    }

    fun clearIdentification() {
        _identificationState.value = PlantIdentificationUiState.Idle
    }

    fun saveIdentifiedPlant(
        nickname: String,
        location: String,
        customNotes: String,
        careInfo: PlantCareInfo
    ) {
        viewModelScope.launch {
            val entity = PlantEntity(
                name = if (nickname.isNotBlank()) nickname else careInfo.commonName,
                botanicalName = careInfo.botanicalName,
                location = location,
                lightNeeds = careInfo.lightNeeds,
                wateringDaysInterval = careInfo.wateringDaysInterval,
                lastWateredTimestamp = System.currentTimeMillis(),
                healthStatus = careInfo.healthStatus,
                notes = customNotes,
                summary = careInfo.summary,
                soilPreference = careInfo.soilPreference,
                toxicity = careInfo.toxicity,
                createdTimestamp = System.currentTimeMillis()
            )
            repository.savePlant(entity)
            _identificationState.value = PlantIdentificationUiState.Idle
        }
    }

    fun markWatered(plantId: Int) {
        viewModelScope.launch {
            repository.markWatered(plantId)
        }
    }

    fun addCareLog(plantId: Int, careType: String, note: String) {
        viewModelScope.launch {
            repository.addCareLog(
                CareLogEntity(
                    plantId = plantId,
                    careType = careType,
                    timestamp = System.currentTimeMillis(),
                    note = note
                )
            )
        }
    }

    fun deletePlant(plant: PlantEntity) {
        viewModelScope.launch {
            repository.deletePlant(plant)
        }
    }

    fun sendChatMessage(text: String, bitmap: Bitmap? = null) {
        if (text.isBlank() && bitmap == null) return
        val userMsg = ChatMessage(sender = ChatSender.USER, text = text)
        val currentMsgs = _chatMessages.value.toMutableList()
        currentMsgs.add(userMsg)
        _chatMessages.value = currentMsgs
        _isChatGenerating.value = true

        viewModelScope.launch {
            val historyPairs = currentMsgs.takeLast(10).map { msg ->
                Pair(if (msg.sender == ChatSender.USER) "USER" else "BOT", msg.text)
            }
            val result = repository.chatWithPlantDoctor(text, historyPairs, bitmap)
            _isChatGenerating.value = false

            result.onSuccess { reply ->
                val botMsg = ChatMessage(sender = ChatSender.BOT, text = reply)
                _chatMessages.value = _chatMessages.value + botMsg
            }.onFailure { err ->
                val botMsg = ChatMessage(
                    sender = ChatSender.BOT,
                    text = "I experienced a connection issue (${err.localizedMessage}). However, as a general rule: ensure your plant receives proper sunlight, avoid overwatering, and ensure good pot drainage!"
                )
                _chatMessages.value = _chatMessages.value + botMsg
            }
        }
    }
}
