package com.example.data.model

object SamplePlantsData {
    val samplePlants: List<PlantCareInfo> = listOf(
        PlantCareInfo(
            commonName = "Monstera Deliciosa",
            botanicalName = "Monstera deliciosa",
            summary = "The iconic Swiss Cheese Plant with large, glossy, perforated tropical leaves.",
            confidence = 98,
            healthStatus = "Healthy",
            lightNeeds = "Bright, indirect sunlight. Avoid direct afternoon sun.",
            waterSchedule = "Water every 7 days when top 2 inches of soil feel dry",
            wateringDaysInterval = 7,
            humidityAndTemp = "65-80°F (18-27°C), High humidity preferred (50%+)",
            soilPreference = "Peat-based potting mix with perlite & orchid bark",
            fertilizerInfo = "Feed monthly in spring and summer with liquid 10-10-10 fertilizer",
            toxicity = "Toxic to cats & dogs if ingested (calcium oxalates)",
            pruningTips = "Wipe leaves regularly with damp cloth. Trim yellowing lower leaves near base.",
            commonIssues = "Yellow leaves indicate overwatering; brown crisp edges signal low humidity.",
            stepByStepCare = listOf(
                "Place near an east or west-facing window with bright filtered light.",
                "Check top 2 inches of soil with finger. Water until liquid runs out pot bottom.",
                "Provide a moss pole or wooden stake for climbing stems.",
                "Wipe dust off glossy leaves monthly to maximize photosynthesis."
            )
        ),
        PlantCareInfo(
            commonName = "Snake Plant (Sansevieria)",
            botanicalName = "Dracaena trifasciata",
            summary = "Ultra-hardy air-purifying plant with upright, sword-like variegated green leaves.",
            confidence = 99,
            healthStatus = "Healthy",
            lightNeeds = "Tolerates low light to bright indirect sunlight",
            waterSchedule = "Water sparingly every 14 to 21 days. Allow soil to dry completely.",
            wateringDaysInterval = 14,
            humidityAndTemp = "55-85°F (13-29°C), Standard household humidity",
            soilPreference = "Succulent & cactus soil mix with coarse sand",
            fertilizerInfo = "Feed 2 times a year during spring & summer",
            toxicity = "Mildly toxic to pets if chewed",
            pruningTips = "Trim damaged leaves at soil level using sterile shears.",
            commonIssues = "Mushy leaves are caused by root rot due to overwatering.",
            stepByStepCare = listOf(
                "Place anywhere from low-light office corners to bright living rooms.",
                "Water strictly only when soil is completely dry throughout pot.",
                "Ensure pot has drainage hole to avoid standing water.",
                "Repot every 2-3 years when roots become tightly bound."
            )
        ),
        PlantCareInfo(
            commonName = "Peace Lily",
            botanicalName = "Spathiphyllum wallisii",
            summary = "Elegant indoor favorite with lush deep green leaves and white spathe blooms.",
            confidence = 97,
            healthStatus = "Healthy",
            lightNeeds = "Low to medium indirect sunlight",
            waterSchedule = "Water every 5 to 7 days when leaves begin to slightly droop",
            wateringDaysInterval = 6,
            humidityAndTemp = "65-80°F (18-27°C), High humidity lover",
            soilPreference = "Rich, peat-heavy, moisture-retaining soil mix",
            fertilizerInfo = "Diluted balanced fertilizer every 6 weeks during growth season",
            toxicity = "Toxic to pets (causes mouth irritation)",
            pruningTips = "Cut faded white flowers at base to encourage new blooms.",
            commonIssues = "Brown leaf tips caused by tap water chemicals or dry air.",
            stepByStepCare = listOf(
                "Keep in shaded or medium indirect light location away from drafts.",
                "Water when foliage shows gentle droop. Use filtered water if possible.",
                "Mist leaves regularly or group with other plants for humidity.",
                "Remove spent blooms promptly at base of stem."
            )
        ),
        PlantCareInfo(
            commonName = "Fiddle Leaf Fig",
            botanicalName = "Ficus lyrata",
            summary = "Dramatic indoor tree with large, violin-shaped glossy dark green foliage.",
            confidence = 95,
            healthStatus = "Healthy",
            lightNeeds = "Bright, consistent indirect light; needs 4-6 hrs filtered sun",
            waterSchedule = "Water every 7 to 10 days when top 3 inches of soil dry out",
            wateringDaysInterval = 8,
            humidityAndTemp = "65-75°F (18-24°C), Avoid cold drafts and AC vents",
            soilPreference = "Well-draining, rich potting soil with drainage gravel",
            fertilizerInfo = "High-nitrogen liquid fertilizer every 4 weeks in spring/summer",
            toxicity = "Toxic to pets if leaves eaten",
            pruningTips = "Pinch top leaves to encourage fuller branching trunk growth.",
            commonIssues = "Brown spots on leaves mean overwatering or sudden location movement.",
            stepByStepCare = listOf(
                "Position near bright window. Rotate pot 90 degrees weekly for even light.",
                "Water deeply until water drains, then empty saucer completely.",
                "Wipe broad leaves clean with damp cloth to remove dust buildup.",
                "Avoid moving plant frequently to prevent leaf drop."
            )
        ),
        PlantCareInfo(
            commonName = "Echeveria Succulent",
            botanicalName = "Echeveria elegans",
            summary = "Compact rosette succulent with fleshy blue-green leaves and coral flowers.",
            confidence = 99,
            healthStatus = "Healthy",
            lightNeeds = "Full sun; at least 6 hours of bright light daily",
            waterSchedule = "Water every 14 days using soak and dry method",
            wateringDaysInterval = 14,
            humidityAndTemp = "60-80°F (15-27°C), Dry climate preferred",
            soilPreference = "Cactus & succulent mix with perlite or pumice",
            fertilizerInfo = "Low-nitrogen succulent food once in early spring",
            toxicity = "Non-toxic to cats, dogs, and humans",
            pruningTips = "Gently pull off dry, dead bottom leaves.",
            commonIssues = "Stretching tall stem (etiolation) means plant needs much more light.",
            stepByStepCare = listOf(
                "Place on south-facing windowsill with direct sunlight.",
                "Soak soil completely until water drips out, then allow to dry 100%.",
                "Keep water off foliage center to prevent rosette rot.",
                "Protect from frost during winter months."
            )
        ),
        PlantCareInfo(
            commonName = "Sweet Basil",
            botanicalName = "Ocimum basilicum",
            summary = "Aromatic culinary herb with tender bright green leaves and sweet flavor.",
            confidence = 98,
            healthStatus = "Healthy",
            lightNeeds = "6-8 hours of direct bright sunlight daily",
            waterSchedule = "Water every 2 to 3 days; keep soil consistently moist",
            wateringDaysInterval = 3,
            humidityAndTemp = "70-90°F (21-32°C), Warm environment mandatory",
            soilPreference = "Moist, fertile, organic-rich well-draining soil",
            fertilizerInfo = "Organic seaweed extract or compost tea every 3 weeks",
            toxicity = "Safe and edible for humans and pets",
            pruningTips = "Pinch top stem tips regularly to encourage bushy growth and prevent flowering.",
            commonIssues = "Leggy stems caused by insufficient sunlight; white powder indicates mildew.",
            stepByStepCare = listOf(
                "Keep on sunniest windowsill or sunny outdoor patio.",
                "Water at soil base in early morning; keep foliage dry.",
                "Pinch off flower buds immediately to preserve leaf flavor.",
                "Harvest outer leaves continuously for cooking."
            )
        )
    )
}
