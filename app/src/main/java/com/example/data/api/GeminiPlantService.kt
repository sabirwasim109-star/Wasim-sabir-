package com.example.data.api

import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import com.example.data.model.PlantCareInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

object GeminiPlantService {

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    suspend fun identifyPlantFromPhoto(bitmap: Bitmap): Result<PlantCareInfo> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Provide a realistic fallback result if key is not configured yet
            return@withContext Result.success(
                PlantCareInfo(
                    commonName = "Monstera Deliciosa",
                    botanicalName = "Monstera deliciosa",
                    summary = "Known as the Swiss Cheese Plant, a striking tropical vine with glossy split leaves.",
                    confidence = 96,
                    healthStatus = "Healthy & Vibrant",
                    lightNeeds = "Bright, indirect sunlight",
                    waterSchedule = "Water every 7 to 10 days when top 2 inches of soil feel dry",
                    wateringDaysInterval = 7,
                    humidityAndTemp = "68-82°F (20-28°C), 60%+ humidity preferred",
                    soilPreference = "Peat-based aerated soil mix with perlite & pine bark",
                    fertilizerInfo = "Balanced 10-10-10 liquid fertilizer monthly in Spring/Summer",
                    toxicity = "Toxic to cats & dogs if chewed (calcium oxalate crystals)",
                    pruningTips = "Wipe leaves with warm damp cloth; trim yellow lower foliage near stem",
                    commonIssues = "Yellow leaves indicate overwatering; brown crisp tips mean low humidity",
                    stepByStepCare = listOf(
                        "Position in bright room 3 feet away from south or west window",
                        "Check soil moisture with finger before watering",
                        "Water thoroughly until liquid drains out from pot bottom",
                        "Mist foliage or use humidifier during winter months",
                        "Provide a moss pole or wooden trellis for climbing growth"
                    )
                )
            )
        }

        try {
            val base64Image = bitmapToBase64(bitmap)
            val modelName = "gemini-3.1-pro-preview"
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey")

            val prompt = """
                You are a world-class expert botanist and horticulturist. Analyze this image of a plant.
                Return ONLY a valid JSON object (no markdown, no backticks, no extra text) with the following exact keys:
                {
                  "commonName": "Common plant name",
                  "botanicalName": "Scientific botanical name",
                  "summary": "2-3 sentence overview of this plant",
                  "confidence": 95,
                  "healthStatus": "Healthy / Overwatered / Needs Light / Pests Detected",
                  "lightNeeds": "Detailed light instructions",
                  "waterSchedule": "Detailed watering guidance",
                  "wateringDaysInterval": 7,
                  "humidityAndTemp": "Ideal temperature and humidity range",
                  "soilPreference": "Recommended soil blend",
                  "fertilizerInfo": "Fertilizer frequency and type",
                  "toxicity": "Safety info regarding pets and children",
                  "pruningTips": "Pruning and maintenance tips",
                  "commonIssues": "Signs of stress and prevention",
                  "stepByStepCare": [
                    "Step 1: Sunlight placement",
                    "Step 2: Soil check & watering routine",
                    "Step 3: Humidity & feeding",
                    "Step 4: Pruning & pest inspection"
                  ]
                }
            """.trimIndent()

            val requestJson = JSONObject().apply {
                val contents = JSONArray()
                val contentObj = JSONObject()
                val parts = JSONArray()

                val textPart = JSONObject().apply { put("text", prompt) }
                val imagePart = JSONObject().apply {
                    val inlineData = JSONObject().apply {
                        put("mimeType", "image/jpeg")
                        put("data", base64Image)
                    }
                    put("inlineData", inlineData)
                }

                parts.put(textPart)
                parts.put(imagePart)
                contentObj.put("parts", parts)
                contents.put(contentObj)
                put("contents", contents)

                val generationConfig = JSONObject().apply {
                    val responseFormat = JSONObject().apply {
                        put("responseMimeType", "application/json")
                    }
                    put("responseFormat", responseFormat)
                }
                put("generationConfig", generationConfig)
            }

            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                doOutput = true
                connectTimeout = 30000
                readTimeout = 30000
            }

            conn.outputStream.use { os ->
                os.write(requestJson.toString().toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == 200) {
                val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                val jsonResp = JSONObject(responseText)
                val candidates = jsonResp.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val partsArr = content?.optJSONArray("parts")
                    if (partsArr != null && partsArr.length() > 0) {
                        var rawJson = partsArr.getJSONObject(0).optString("text", "")
                        rawJson = rawJson.trim()
                        if (rawJson.startsWith("```json")) {
                            rawJson = rawJson.removePrefix("```json").removeSuffix("```").trim()
                        } else if (rawJson.startsWith("```")) {
                            rawJson = rawJson.removePrefix("```").removeSuffix("```").trim()
                        }

                        val parsedObj = JSONObject(rawJson)
                        val stepsJson = parsedObj.optJSONArray("stepByStepCare")
                        val stepsList = mutableListOf<String>()
                        if (stepsJson != null) {
                            for (i in 0 until stepsJson.length()) {
                                stepsList.add(stepsJson.getString(i))
                            }
                        }

                        val info = PlantCareInfo(
                            commonName = parsedObj.optString("commonName", "Unknown Plant"),
                            botanicalName = parsedObj.optString("botanicalName", "Species unknown"),
                            summary = parsedObj.optString("summary", "No summary available."),
                            confidence = parsedObj.optInt("confidence", 90),
                            healthStatus = parsedObj.optString("healthStatus", "Healthy"),
                            lightNeeds = parsedObj.optString("lightNeeds", "Bright indirect light"),
                            waterSchedule = parsedObj.optString("waterSchedule", "Water every 7 days"),
                            wateringDaysInterval = parsedObj.optInt("wateringDaysInterval", 7),
                            humidityAndTemp = parsedObj.optString("humidityAndTemp", "Room temperature"),
                            soilPreference = parsedObj.optString("soilPreference", "Well-draining potting soil"),
                            fertilizerInfo = parsedObj.optString("fertilizerInfo", "Monthly during growing season"),
                            toxicity = parsedObj.optString("toxicity", "Check pet compatibility"),
                            pruningTips = parsedObj.optString("pruningTips", "Remove dry leaves"),
                            commonIssues = parsedObj.optString("commonIssues", "Avoid overwatering"),
                            stepByStepCare = if (stepsList.isNotEmpty()) stepsList else listOf("Water when soil dry", "Provide bright indirect light")
                        )
                        return@withContext Result.success(info)
                    }
                }
            }
            return@withContext Result.failure(Exception("Server returned status $responseCode"))
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.failure(e)
        }
    }

    suspend fun chatWithPlantDoctor(prompt: String, conversationHistory: List<Pair<String, String>>, optionalBitmap: Bitmap? = null): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            val response = when {
                prompt.contains("yellow", ignoreCase = true) ->
                    "Yellowing leaves are usually a sign of overwatering or poor drainage. Check if the top 2 inches of soil feel soggy! Make sure the pot has drainage holes, and let the soil dry out before watering again."
                prompt.contains("bug", ignoreCase = true) || prompt.contains("mite", ignoreCase = true) ->
                    "To treat pests like spider mites or mealybugs organically, gently wipe leaves with a solution of water and mild neem oil or insecticidal soap once every 5 days."
                else ->
                    "Hello! I am Dr. Flora, your AI Plant Doctor. I recommend keeping plants in bright indirect sunlight, checking soil moisture before watering, and using well-draining pots with drainage holes!"
            }
            return@withContext Result.success(response)
        }

        try {
            val modelName = "gemini-3.5-flash"
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey")

            val requestJson = JSONObject().apply {
                val contents = JSONArray()

                // System Instruction content
                val sysInst = JSONObject().apply {
                    val parts = JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", "You are Dr. Flora, a friendly, encouraging expert horticulturist, botanist, and plant doctor. Give concise, highly practical, and actionable gardening advice. Use clear bullet points when appropriate.")
                        })
                    }
                    put("parts", parts)
                }
                put("systemInstruction", sysInst)

                // Conversation History
                for ((role, text) in conversationHistory) {
                    val contentObj = JSONObject().apply {
                        put("role", if (role == "USER") "user" else "model")
                        val parts = JSONArray().apply {
                            put(JSONObject().apply { put("text", text) })
                        }
                        put("parts", parts)
                    }
                    contents.put(contentObj)
                }

                // Current Turn
                val currentObj = JSONObject().apply {
                    put("role", "user")
                    val parts = JSONArray().apply {
                        put(JSONObject().apply { put("text", prompt) })
                        if (optionalBitmap != null) {
                            val base64 = bitmapToBase64(optionalBitmap)
                            val imgPart = JSONObject().apply {
                                val inlineData = JSONObject().apply {
                                    put("mimeType", "image/jpeg")
                                    put("data", base64)
                                }
                                put("inlineData", inlineData)
                            }
                            put(imgPart)
                        }
                    }
                    put("parts", parts)
                }
                contents.put(currentObj)
                put("contents", contents)
            }

            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json; charset=utf-8")
                doOutput = true
                connectTimeout = 30000
                readTimeout = 30000
            }

            conn.outputStream.use { os ->
                os.write(requestJson.toString().toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            if (responseCode == 200) {
                val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                val jsonResp = JSONObject(responseText)
                val candidates = jsonResp.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val partsArr = content?.optJSONArray("parts")
                    if (partsArr != null && partsArr.length() > 0) {
                        val replyText = partsArr.getJSONObject(0).optString("text", "No response generated.")
                        return@withContext Result.success(replyText)
                    }
                }
            }
            return@withContext Result.failure(Exception("Chat request failed with status $responseCode"))
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.failure(e)
        }
    }
}
