package com.rmxdev.braillex.presenter.initial

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rmxdev.braillex.R
import com.rmxdev.braillex.data.network.QrScannerActivity
import com.rmxdev.braillex.ui.theme.BarColor
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.White

@Composable
fun InitialScreen(
    modifier: Modifier = Modifier,
    navigateToAccount: () -> Unit,
    navigateToMedia: (String) -> Unit,
    viewModel: InitialViewModel = hiltViewModel(),
) {

    val systemUiController = rememberSystemUiController()
    val statusBarColor = DarkBlack
    val context = LocalContext.current
    val scannedQr by viewModel.scannedQrContent.collectAsState()

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val fileId = result.data?.getStringExtra("SCANNED_FILE_ID")
            fileId?.let { viewModel.processScannedQr(it) }
        }
    }

    LaunchedEffect(scannedQr) {
        Log.d("InitialScreen", "LaunchedEffect triggered with scannedQr: $scannedQr")
        scannedQr?.let { audioUrl ->
            Log.d("InitialScreen", "scannedQr updated: $scannedQr")
            navigateToMedia(audioUrl)
        }
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor
        )
        systemUiController.setNavigationBarColor(
            color = statusBarColor
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlack)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Row(
            modifier = Modifier
                .background(BarColor)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painterResource(id = R.drawable.logotipo),
                contentDescription = "Logo",
                tint = Color.Unspecified,
            )
            IconButton(onClick = { navigateToAccount() }) {
                Icon(
                    painterResource(id = R.drawable.ic_account), contentDescription = "Account",
                    modifier = Modifier
                        .background(White)
                        .size(40.dp),
                )
            }
        }
        IconButton(
            onClick = {
                val intent = Intent(context, QrScannerActivity::class.java)
                scannerLauncher.launch(intent)
            },
            modifier = Modifier
                .size(550.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.botonqr),
                contentDescription = "QR",
                tint = Color.Unspecified,
            )
        }
        Text(text = "Escanea el código QR", fontSize = 30.sp, color = White)
    }
}