package com.rmxdev.braillex.presenter.reproductor

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.rmxdev.braillex.R
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.ui.theme.Blue
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.White

@Composable
fun ReproductorScreen(
    modifier: Modifier = Modifier,
    viewModel: ReproductorViewModel = hiltViewModel(),
    navigateToInitial: () -> Unit,
    pdfFile: PdfFile,
    navigateToMedia: (audioUrl: String) -> Unit,
) {
    LaunchedEffect(pdfFile) {
        viewModel.setPdfFile(pdfFile)
    }

    val reproductorState = viewModel.reproductorState.collectAsState()
    val qrCodeUrl = viewModel.getQrCodeUrl()
    val title = viewModel.getFileTitle()

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
                    viewModel.deleteAudioFile { success -> if (success) navigateToInitial() }
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
        qrCodeUrl.let {
            val painter = rememberAsyncImagePainter(model = it)
            Image(
                painter = painter,
                contentDescription = "QR Code",
                modifier = Modifier.size(200.dp)
            )
        }
        Text(
            text = title ,
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = {
                navigateToMedia(pdfFile.audioUrl)
            },
            colors = buttonColors(containerColor = Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(50.dp)
        ) {
            Text(text = "Reproducir", fontSize = 25.sp, color = White, fontWeight = FontWeight.Bold)
        }

        when (reproductorState.value) {
            is ReproductorState.Loading -> {
                CircularProgressIndicator()
            }

            is ReproductorState.Success -> {
                navigateToMedia(pdfFile.audioUrl)
            }

            is ReproductorState.Error -> {
                Text(text = "Error: ${(reproductorState.value as ReproductorState.Error).message}")
            }
        }

    }
}