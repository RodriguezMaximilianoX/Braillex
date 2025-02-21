package com.rmxdev.braillex.presenter.media

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MediaScreen(
    modifier: Modifier = Modifier,
    backButton: () -> Unit,
    audioUrl: String,
    viewModel: MediaViewModel = hiltViewModel()
) {
    val isPrepared by remember { viewModel.isPrepared } // Observar cambios en el estado

    LaunchedEffect(audioUrl) {
        viewModel.initializePlayer(audioUrl)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Reproduciendo audio")

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                if (isPrepared) {
                    viewModel.play()
                } else {
                    Log.d("MediaPlayer", "El audio aún no está listo")
                }
            }) {
                Text(text = "Reproducir")
            }
            Button(onClick = { viewModel.pause() }) {
                Text(text = "Pausar")
            }
            Button(onClick = { viewModel.stop() }) {
                Text(text = "Detener")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { backButton() }) {
            Text("Volver")
        }
    }
}