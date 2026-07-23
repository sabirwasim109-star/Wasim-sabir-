package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.navigation.NavRoutes
import com.example.ui.screens.CareGuideLibraryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyGardenScreen
import com.example.ui.screens.PlantDetailScreen
import com.example.ui.screens.PlantDoctorChatScreen
import com.example.ui.screens.PlantIdentifyScreen
import com.example.ui.theme.FloraCareTheme
import com.example.ui.viewmodel.PlantViewModel

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {

    private val viewModel: PlantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FloraCareTheme {
                FloraCareApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FloraCareApp(viewModel: PlantViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem(NavRoutes.HOME, "Home", Icons.Default.Home),
        BottomNavItem(NavRoutes.IDENTIFY, "Identify", Icons.Default.AddAPhoto),
        BottomNavItem(NavRoutes.MY_GARDEN, "My Garden", Icons.Default.LocalFlorist),
        BottomNavItem(NavRoutes.PLANT_DOCTOR, "Plant Doctor", Icons.Default.Chat),
        BottomNavItem(NavRoutes.CARE_LIBRARY, "Guides", Icons.Default.MenuBook)
    )

    val showBottomBar = currentRoute in listOf(
        NavRoutes.HOME,
        NavRoutes.IDENTIFY,
        NavRoutes.MY_GARDEN,
        NavRoutes.PLANT_DOCTOR,
        NavRoutes.CARE_LIBRARY
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
                    shadowElevation = 8.dp
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                                label = { Text(item.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.HOME) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToIdentify = { navController.navigate(NavRoutes.IDENTIFY) },
                    onNavigateToMyGarden = { navController.navigate(NavRoutes.MY_GARDEN) },
                    onNavigateToPlantDetail = { plantId ->
                        navController.navigate(NavRoutes.plantDetailRoute(plantId))
                    },
                    onNavigateToPlantDoctor = { navController.navigate(NavRoutes.PLANT_DOCTOR) },
                    onNavigateToCareLibrary = { navController.navigate(NavRoutes.CARE_LIBRARY) }
                )
            }

            composable(NavRoutes.IDENTIFY) {
                PlantIdentifyScreen(
                    viewModel = viewModel,
                    onNavigateToMyGarden = { navController.navigate(NavRoutes.MY_GARDEN) }
                )
            }

            composable(NavRoutes.MY_GARDEN) {
                MyGardenScreen(
                    viewModel = viewModel,
                    onNavigateToPlantDetail = { plantId ->
                        navController.navigate(NavRoutes.plantDetailRoute(plantId))
                    },
                    onNavigateToIdentify = { navController.navigate(NavRoutes.IDENTIFY) }
                )
            }

            composable(
                route = NavRoutes.PLANT_DETAIL,
                arguments = listOf(navArgument("plantId") { type = NavType.IntType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getInt("plantId") ?: 0
                PlantDetailScreen(
                    plantId = plantId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(NavRoutes.PLANT_DOCTOR) {
                PlantDoctorChatScreen(viewModel = viewModel)
            }

            composable(NavRoutes.CARE_LIBRARY) {
                CareGuideLibraryScreen()
            }
        }
    }
}
