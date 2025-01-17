package com.rmxdev.braillex.presenter

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rmxdev.braillex.presenter.account.AccountScreen
import com.rmxdev.braillex.presenter.initial.InitialScreen
import com.rmxdev.braillex.presenter.login.LoginScreen
import com.rmxdev.braillex.presenter.email.EmailScreen
import com.rmxdev.braillex.presenter.signup.SignupScreen

@Composable
fun NavigationWrapper(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        composable("initial") {
            InitialScreen(
                modifier = Modifier,
                navigateToAccount = { navController.navigate("account") },
                navigateToFiles = { navController.navigate("files") }
            )
        }
        composable("account") {
            AccountScreen(
                modifier = Modifier,
                navigateToLogin = { navController.navigate("login") },
                navigateToEmail = { navController.navigate("userEmail") },
                navigateToHelp = { navController.navigate("help") },
                backButton = { navController.popBackStack() }
            )
        }
        composable("login") {
            LoginScreen(
                modifier = Modifier,
                backButton = { navController.popBackStack() },
                navigateToEmail = { navController.navigate("userEmail") },
                navigateToHelp = { navController.navigate("help") },
                onLoginSuccess = { navController.navigate("initial") }
            )
        }
        composable("userEmail") {
            EmailScreen(
                modifier = Modifier,
                backButton = { navController.popBackStack() },
                navigateToSignup = {
                    val userEmail = it
                    navController.navigate("signup/$userEmail")
                },
                navigateToHelp = { navController.navigate("help") }
            )
        }
        composable("signup/{userEmail}") {backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail")
            SignupScreen(
                modifier = Modifier,
                backButton = { navController.popBackStack() },
                userEmail = userEmail ?: "",
                navigateToHelp = { navController.navigate("help") },
                onLoginSuccess = { navController.navigate("initial") }
            )
        }
    }
}