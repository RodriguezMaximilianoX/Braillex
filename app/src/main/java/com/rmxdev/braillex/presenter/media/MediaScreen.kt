package com.rmxdev.braillex.presenter.media

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var isPlaying by rememberSaveable { mutableStateOf(false) }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor
        )
        systemUiController.setNavigationBarColor(
            color = statusBarColor
        )
    }

    LaunchedEffect(audioUrl) {
        viewModel.initializePlayer(audioUrl)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBlack),
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
                IconButton(onClick = { backButton() }, modifier = Modifier.size(70.dp)) {
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
            Spacer(modifier = Modifier.weight(1f))
            Row() {
                IconButton(
                    onClick = {}
                ){
                    Icon(
                        painterResource(id = R.drawable.previousbutton),
                        contentDescription = "Previous",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                IconButton(
                    onClick = { if (isPlaying) viewModel.pause() else viewModel.play() },
                ) {
                    val buttonState =
                        if (isPlaying) R.drawable.pausebutton else R.drawable.playbutton
                    Icon(
                        painterResource(id = buttonState),
                        contentDescription = "Play",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painterResource(id = R.drawable.nextbutton),
                        contentDescription = "Next",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }


    }
}