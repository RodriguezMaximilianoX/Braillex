package com.rmxdev.braillex.presenter.initial

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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rmxdev.braillex.R
import com.rmxdev.braillex.ui.theme.BarColor
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.White

@Composable
fun InitialScreen(
    modifier: Modifier = Modifier,
    navigateToAccount: () -> Unit,
    navigateToFiles: () -> Unit
) {

    val systemUiController = rememberSystemUiController()
    val statusBarColor = DarkBlack

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
            onClick = { navigateToFiles() },
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