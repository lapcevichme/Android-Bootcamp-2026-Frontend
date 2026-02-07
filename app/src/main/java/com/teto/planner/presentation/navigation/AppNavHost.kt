package com.teto.planner.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.teto.planner.presentation.features.auth.login.LoginScreen
import com.teto.planner.presentation.features.auth.register.RegisterScreen
import com.teto.planner.presentation.features.meeting_create.MeetingCreateScreen
import com.teto.planner.presentation.features.profile.ProfileScreen
import com.teto.planner.presentation.features.schedule.ScheduleScreen
import com.teto.planner.presentation.features.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToSchedule = {
                    navController.navigate(Screen.Schedule.route) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = hiltViewModel(),
                onLoginSuccess = {
                    navController.navigate(Screen.Schedule.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = hiltViewModel(),
                onRegisterSuccess = {
                    navController.navigate(Screen.Schedule.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Schedule.route) {
            ScheduleScreen(
                onCreateMeeting = { navController.navigate(Screen.MeetingCreate.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                viewModel = hiltViewModel()
            )
        }

        composable(Screen.Invitations.route) {
            TestScreenLayout(title = "Входящие приглашения") {
                Text("Здесь будет список приглашений")
            }
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onSave = { navController.popBackStack() },
                onEdit = {},
                onExit = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MeetingCreate.route) {
            MeetingCreateScreen(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
    }
}


@Composable
fun TestScreenLayout(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        content()
    }
}