package com.lovekey.clone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lovekey.clone.ui.theme.LoveKeyCloneTheme
import com.lovekey.clone.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the app fullscreen (draw behind system bars)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            LoveKeyCloneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoveKeyApp()
                }
            }
        }
    }
}

@Composable
fun LoveKeyApp() {
    val navController = rememberNavController()
    var selectedIds by remember { mutableStateOf<List<String>>(emptyList()) }

    val togglePersona: (String) -> Unit = { id ->
        selectedIds = if (selectedIds.contains(id)) selectedIds.filter { it != id } else selectedIds + id
    }

    NavHost(navController = navController, startDestination = "step0") {
        composable("step0") {
            WelcomeScreen(onNext = { navController.navigate("step1") })
        }
        composable("step1") {
            GenderScreen(onNext = { navController.navigate("step2") })
        }
        composable("step2") {
            BirthdayScreen(onPrev = { navController.popBackStack() }, onNext = { navController.navigate("step3") })
        }
        composable("step3") {
            PersonaScreen(onPrev = { navController.popBackStack() }, onNext = { navController.navigate("step4") }, selectedIds = selectedIds, onToggle = togglePersona)
        }
        composable("step4") {
            LoadingScreen(onNext = { navController.navigate("step5") })
        }
        composable("step5") {
            SetupKeyboardScreen(onNext = { navController.navigate("step6") })
        }
        composable("step6") {
            KeyboardSelectScreen(onNext = { navController.navigate("step7") })
        }
        composable("step7") {
            ChatKeyboardScreen(isTutorial = true, onComplete = { navController.navigate("step8") })
        }
        composable("step8") {
            PaywallMainScreen(onNext = { navController.navigate("step10") }, onDownsell = { navController.navigate("step10") })
        }
        composable("step9") {
            ChatKeyboardScreen(isTutorial = false, onComplete = { /* handle exit from standalone keyboard chat view */ })
        }
        composable("step10") {
            MainAppShell()
        }
    }
}
