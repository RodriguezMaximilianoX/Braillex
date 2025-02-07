package com.rmxdev.braillex.presenter

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.presenter.account.AccountScreen
import com.rmxdev.braillex.presenter.email.EmailScreen
import com.rmxdev.braillex.presenter.files.FileScreen
import com.rmxdev.braillex.presenter.initial.InitialScreen
import com.rmxdev.braillex.presenter.login.LoginScreen
import com.rmxdev.braillex.presenter.media.MediaScreen
import com.rmxdev.braillex.presenter.newFile.NewFileScreen
import com.rmxdev.braillex.presenter.newFile.TitleScreen
import com.rmxdev.braillex.presenter.reproductor.ReproductorScreen
import com.rmxdev.braillex.presenter.signup.SignupScreen

@Composable
fun NavigationWrapper(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    gson: Gson,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
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
                onLoginSuccess = { navController.navigate("files") }
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
                onFileSelected = { pdfUri ->
                    val encodedUri = Uri.encode(pdfUri.toString())
                    navController.navigate("title/$encodedUri")
                }
            )
        }
        composable("title/{encodedUri}") { backStackEntry ->
            val fileUriStr = backStackEntry.arguments?.getString("encodedUri")
            val fileUri = fileUriStr?.let { Uri.parse(it) }
            TitleScreen(
                modifier = Modifier,
                navigateToInitial = { navController.navigate("initial") },
                navigateToHelp = { navController.navigate("help") },
                navigateToUpload = { pdfUri, pdfTitle ->
                    val encodedUri = Uri.encode(pdfUri.toString())
                    navController.navigate("newFile/$pdfTitle/$encodedUri")
                },
                fileUri = fileUri.takeIf { it != Uri.EMPTY } ?: Uri.EMPTY
            )
        }

        composable("newFile/{pdfTitle}/{encodedUri}") { backStackEntry ->
            val pdfTitle = backStackEntry.arguments?.getString("pdfTitle")
            val pdfUri = backStackEntry.arguments?.getString("encodedUri")
            val fileUri = Uri.parse(pdfUri)
            NewFileScreen(
                modifier = Modifier,
                navigateToInitial = { navController.navigate("initial") },
                navigateToHelp = { navController.navigate("help") },
                navigateToReproductor = {
                    val pdfFileJson = Uri.encode(gson.toJson(it))
                    navController.navigate("reproductor/$pdfFileJson")
                },
                pdfTitle = pdfTitle ?: "",
                pdfUri = fileUri
            )
        }
        composable("reproductor/{pdfFile}") { backStackEntry ->
            val pdfFileJson = backStackEntry.arguments?.getString("pdfFile")
            val pdfFile = gson.fromJson(Uri.decode(pdfFileJson), PdfFile::class.java)
            ReproductorScreen(
                modifier = Modifier,
                navigateToInitial = { navController.navigate("initial") },
                pdfFile = pdfFile,
                navigateToMedia = { audioUrl ->
                    navController.navigate("media/$audioUrl")
                }
            )
        }
        composable("media/{audioUrl}") { backStackEntry ->
            val audioUrl = backStackEntry.arguments?.getString("audioUrl")
            MediaScreen(
                modifier = Modifier,
                backButton = { navController.popBackStack() },
                audioUrl = audioUrl ?: ""
            )
        }
    }
}