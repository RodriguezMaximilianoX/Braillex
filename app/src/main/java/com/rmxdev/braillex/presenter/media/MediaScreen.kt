package com.rmxdev.braillex.presenter.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rmxdev.braillex.R
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.White

@Composable
fun MediaScreen(
    modifier: Modifier = Modifier,
    backButton: () -> Unit,
    audioUrl: String,
    viewModel: MediaViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = DarkBlack
    val isPrepared by remember { viewModel.isPrepared }
    val isPlaying by remember { viewModel.isPlaying }
    val text = if (isPlaying) "Pausar" else "Reproducir"

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor)
        systemUiController.setNavigationBarColor(color = statusBarColor)
    }

    LaunchedEffect(audioUrl) {
        viewModel.initializePlayer(audioUrl)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlack)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    when {
                        pan.x > 100 -> {
                            viewModel.seekForward()
                            viewModel.updateButtonState("forwardAudio")
                        } // Deslizar derecha
                        pan.x < -100 -> {
                            viewModel.seekBackward()
                            viewModel.updateButtonState("backwardAudio")
                        } // Deslizar izquierda
                        pan.y > 100 -> {
                            backButton()
                            viewModel.updateButtonState("home")
                            viewModel.stop()
                        } // Deslizar hacia abajo
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bk_reproductor),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        backButton()
                        viewModel.stop()
                    },
                    modifier = Modifier.size(70.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.backbuttonblack),
                        contentDescription = "Volver",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(0.6f))
                Text(
                    text = "Reproductor",
                    fontSize = 20.sp,
                    color = White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(White)
            )
            Spacer(modifier = Modifier.weight(2f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { viewModel.seekBackward() },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(100.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.previousbutton),
                        contentDescription = "Retroceder",
                        tint = Color.Unspecified
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.playPause()
                        viewModel.updateButtonState("playPause")
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp)

                ) {
                    Icon(
                        painterResource(id = viewModel.buttonState.intValue),
                        contentDescription = viewModel.buttonDescription.value,
                        tint = Color.Unspecified
                    )
                }
                IconButton(
                    onClick = { viewModel.seekForward() },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(100.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.nextbutton),
                        contentDescription = "Adelantar",
                        tint = Color.Unspecified
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text, fontSize = 32.sp, color = White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(0.75f))
        }
        if (!isPrepared) {
            CircularProgressIndicator()
        }
    }
}