package com.example.ui.navigation

object NavRoutes {
    const val HOME = "home"
    const val IDENTIFY = "identify"
    const val MY_GARDEN = "my_garden"
    const val PLANT_DETAIL = "plant_detail/{plantId}"
    const val PLANT_DOCTOR = "plant_doctor"
    const val CARE_LIBRARY = "care_library"

    fun plantDetailRoute(plantId: Int): String = "plant_detail/$plantId"
}
