package com.everybirr.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.everybirr.ui.screen.HomeScreen
import com.everybirr.ui.screen.OnboardingScreen
import com.everybirr.ui.viewmodel.AppViewModel

@Composable
fun EveryBirrNavHost() {
    val navController = rememberNavController()
    val vm: AppViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(onContinue = {
                vm.ensureCurrentMonth()
                navController.navigate("home") { popUpTo("onboarding") { inclusive = true } }
            })
        }
        composable("home") {
            HomeScreen(vm)
        }
    }
}
