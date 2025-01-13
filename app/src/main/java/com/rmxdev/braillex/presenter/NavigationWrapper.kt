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

@Composable
fun NavigationWrapper(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "initial", modifier = modifier) {
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
                navigateToSignup = { navController.navigate("signup") },
                navigateToHelp = { navController.navigate("help") },
                backButton = { navController.popBackStack() }
            )
        }
        composable("login"){
            LoginScreen()
        }
    }
}