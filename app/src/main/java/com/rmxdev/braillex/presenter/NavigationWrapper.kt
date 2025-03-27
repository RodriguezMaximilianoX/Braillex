package com.rmxdev.braillex.presenter

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.rmxdev.braillex.presenter.account.AccountScreen
import com.rmxdev.braillex.presenter.files.FileScreen
import com.rmxdev.braillex.presenter.help.HelpScreen
import com.rmxdev.braillex.presenter.initial.InitialScreen
import com.rmxdev.braillex.presenter.login.LoginScreen
import com.rmxdev.braillex.presenter.media.MediaScreen
import com.rmxdev.braillex.presenter.newFile.NewFileScreen
import com.rmxdev.braillex.presenter.newFile.TitleScreen
import com.rmxdev.braillex.presenter.reproductor.ReproductorScreen
import com.rmxdev.braillex.presenter.signup.EmailScreen
import com.rmxdev.braillex.presenter.signup.SignupScreen
import com.rmxdev.braillex.presenter.support.SupportScreen

@Composable
fun NavigationWrapper(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable("initial") {
            val user = FirebaseAuth.getInstance().currentUser
            val route = if (user != null) "files" else "login"
            InitialScreen(
                modifier = Modifier,
                navigateToAccount = { navController.navigate(route) },
                navigateToMedia = { audioUrl ->
                    val encodedUrl = Uri.encode(audioUrl)
                    navController.navigate("media/$encodedUrl")
                }
            )
        }
        composable("account") {
            AccountScreen(modifier = Modifier,
                navigateToLogin = { navController.navigate("login") },
                navigateToEmail = { navController.navigate("userEmail") },
                navigateToHelp = { navController.navigate("help") },
                backButton = { navController.popBackStack() })
        }
        composable("login") {
            LoginScreen(modifier = Modifier,
                backButton = { navController.popBackStack() },
                navigateToEmail = { navController.navigate("userEmail") },
                navigateToHelp = { navController.navigate("help") },
                onLoginSuccess = { navController.navigate("files") })
        }
        composable("userEmail") {
            EmailScreen(modifier = Modifier,
                backButton = { navController.popBackStack() },
                navigateToSignup = {
                    val userEmail = it
                    navController.navigate("signup/$userEmail")
                },
                navigateToHelp = { navController.navigate("help") })
        }
        composable("signup/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail")
            SignupScreen(modifier = Modifier,
                backButton = { navController.popBackStack() },
                userEmail = userEmail ?: "",
                navigateToHelp = { navController.navigate("help") },
                onLoginSuccess = { navController.navigate("files") })
        }
        composable("files") {
            FileScreen(modifier = Modifier,
                navigateToInitial = { navController.navigate("initial") },
                navigateToHelp = { navController.navigate("help") },
                onFileSelected = { pdfUri ->
                    val encodedUri = Uri.encode(pdfUri.toString())
                    navController.navigate("title/$encodedUri")
                },
                navigateToReproductor = { fileId ->
                    navController.navigate("reproductor/$fileId")
                }
            )
        }
        composable("title/{encodedUri}") { backStackEntry ->
            val fileUriStr = backStackEntry.arguments?.getString("encodedUri")
            val fileUri = fileUriStr?.let { Uri.parse(it) }
            TitleScreen(modifier = Modifier,
                navigateToInitial = { navController.navigate("initial") },
                navigateToHelp = { navController.navigate("help") },
                navigateToUpload = { pdfUri, pdfTitle ->
                    val encodedUri = Uri.encode(pdfUri.toString())
                    navController.navigate("newFile/$pdfTitle/$encodedUri")
                },
                fileUri = fileUri.takeIf { it != Uri.EMPTY } ?: Uri.EMPTY)
        }

        composable("newFile/{pdfTitle}/{encodedUri}") { backStackEntry ->
            val pdfTitle = backStackEntry.arguments?.getString("pdfTitle")
            val pdfUri = backStackEntry.arguments?.getString("encodedUri")
            val fileUri = Uri.parse(pdfUri)
            NewFileScreen(
                modifier = Modifier,
                navigateToInitial = { navController.navigate("initial") },
                navigateToHelp = { navController.navigate("help") },
                navigateToReproductor = { fileId ->
                    navController.navigate("reproductor/$fileId")
                },
                pdfTitle = pdfTitle ?: "",
                pdfUri = fileUri
            )
        }
        composable("reproductor/{fileId}") {
            val fileId = it.arguments?.getString("fileId")
            ReproductorScreen(
                modifier = Modifier,
                fileId = fileId ?: "",
                navigateToInitial = { navController.navigate("initial") },
                navigateToMedia = { audioUrl ->
                    val encodeUri = Uri.encode(audioUrl)
                    navController.navigate("media/$encodeUri")
                })
        }
        composable("media/{audioUrl}") { backStackEntry ->
            val user = FirebaseAuth.getInstance().currentUser
            val route = if (user != null) "files" else "initial"
            val encodedUrl = backStackEntry.arguments?.getString("audioUrl") ?: ""
            Log.d("MediaScreen", "Encode URL: $encodedUrl")
            MediaScreen(
                modifier = Modifier,
                audioUrl = encodedUrl,
                navigateToFiles = { navController.navigate(route) }
            )
        }
        composable("help") {
            HelpScreen(
                modifier = Modifier,
                backButton = { navController.popBackStack() },
                navigateToSupport = { navController.navigate("support") },
                navigateToInitial = { navController.navigate("initial") }
            )
        }
        composable("support") {
            SupportScreen(
                modifier = Modifier,
                backButton = { navController.popBackStack() }
            )
        }
    }
}