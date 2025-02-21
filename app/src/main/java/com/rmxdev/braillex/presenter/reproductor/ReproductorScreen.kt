package com.rmxdev.braillex.presenter.reproductor

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.rmxdev.braillex.R
import com.rmxdev.braillex.ui.theme.Blue
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.White

@Composable
fun ReproductorScreen(
    modifier: Modifier = Modifier,
    fileId: String,
    viewModel: ReproductorViewModel = hiltViewModel(),
    navigateToInitial: () -> Unit,
    navigateToMedia: (String) -> Unit,
) {

    val reproductorState by viewModel.reproductorState.collectAsState()
    val qrCodeBitmap = viewModel.qrCodeBitmap.collectAsState().value
    val title = viewModel.audioTitle.collectAsState().value
    val audioUrl = viewModel.audioUrl.collectAsState().value
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navigateToInitial() }, modifier = Modifier.size(70.dp)) {
                Icon(
                    painterResource(id = R.drawable.homebutton),
                    contentDescription = "Volver al inicio",
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(text = "Mis archivos", fontSize = 20.sp, color = DarkBlack)
            IconButton(
                onClick = {
                    viewModel.deleteAudio(fileId)
                },
                modifier = Modifier.size(70.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.settingbutton),
                    contentDescription = "Ir a configuraciones",
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        qrCodeBitmap?.let { bitmap ->
            val painter = rememberAsyncImagePainter(model = bitmap)
            Image(
                painter = painter,
                contentDescription = "QR Code",
                modifier = Modifier.size(200.dp)
            )
        }
        Text(
            text = title,
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = {
                viewModel.shareQrCode(context)
            },
            colors = buttonColors(containerColor = Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Compartir QR",
                fontSize = 25.sp,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = {
                navigateToMedia(audioUrl)
                Log.d("ReproductorScreen", "Audio URL: $audioUrl")
            },
            colors = buttonColors(containerColor = Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(50.dp)
        ) {
            Text(
                text = "Reproducir",
                fontSize = 25.sp,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
        TextButton(
            onClick = { navigateToInitial() },
            colors = buttonColors(containerColor = Color.Transparent, contentColor = Blue),
            modifier = Modifier
        ) {
            Text(
                text = "Volver al inicio",
                fontSize = 25.sp,
                color = DarkBlack,
                fontWeight = FontWeight.Bold
            )
        }
    }

    when (reproductorState) {
        is ReproductorState.Initial -> {
            viewModel.loadedData(fileId)
        }

        is ReproductorState.Loading -> {
            CircularProgressIndicator()
        }

        is ReproductorState.Deleted -> {
            LaunchedEffect(context) {
                Toast.makeText(context, "Audio eliminado", Toast.LENGTH_LONG).show()
            }
            navigateToInitial()
        }

        is ReproductorState.Success -> {

        }

        is ReproductorState.Error -> {
            Text(
                text = "Error: ${(reproductorState as ReproductorState.Error).message}",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}