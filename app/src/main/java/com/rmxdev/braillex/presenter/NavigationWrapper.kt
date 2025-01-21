package com.rmxdev.braillex.presenter

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rmxdev.braillex.presenter.account.AccountScreen
import com.rmxdev.braillex.presenter.email.EmailScreen
import com.rmxdev.braillex.presenter.files.FileScreen
import com.rmxdev.braillex.presenter.initial.InitialScreen
import com.rmxdev.braillex.presenter.login.LoginScreen
import com.rmxdev.braillex.presenter.newFile.NewFileScreen
import com.rmxdev.braillex.presenter.newFile.TitleScreen
import com.rmxdev.braillex.presenter.signup.SignupScreen

@Composable
fun NavigationWrapper(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "files", modifier = modifier) {
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
        composable("signup/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail")
            SignupScreen(
                modifier = Modifier,
                backButton = { navController.popBackStack() },
                userEmail = userEmail ?: "",
                navigateToHelp = { navController.navigate("help") },
                onLoginSuccess = { navController.navigate("initial") }
            )
        }
        composable("files") {
            FileScreen(
                modifier = Modifier,
                navigateToInitial = { navController.navigate("initial") },
                navigateToHelp = { navController.navigate("help") },
                navigateToPdfTitle = {
                    val pdfUri = it
                    navController.navigate("titleScreen/$pdfUri")
                }
            )
        }
        composable("titleScreen/{pdfUri}") { backStackEntry ->
            val pdfUri = backStackEntry.arguments?.getString("pdfUri")
            TitleScreen(
                modifier = Modifier,
                navigateToInitial = { navController.navigate("initial") },
                navigateToHelp = { navController.navigate("help") },
                navigateToUpload = { pdfTitle, pdfUri ->
                    val pdfTitle = pdfTitle
                    val pdfUri = pdfUri
                    navController.navigate("newFile/$pdfTitle/$pdfUri")
                },
                pdfUri = pdfUri ?: "",
            )
        }

        composable("newFile/{pdfTitle}/{pdfUri}") { backStackEntry ->
            val pdfTitle = backStackEntry.arguments?.getString("pdfTitle")
            val pdfUri = backStackEntry.arguments?.getString("pdfUri")
            NewFileScreen(
                modifier = Modifier,
                navigateToInitial = { navController.navigate("initial") },
                navigateToHelp = { navController.navigate("help") },
                navigateToFiles = { navController.navigate("files") },
                pdfTitle = pdfTitle ?: "",
                pdfUri = pdfUri ?: ""
            )
        }

    }
}