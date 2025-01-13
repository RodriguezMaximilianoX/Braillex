package com.rmxdev.braillex.presenter.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rmxdev.braillex.R
import com.rmxdev.braillex.ui.theme.BarColor
import com.rmxdev.braillex.ui.theme.Blue
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.OffWhite
import com.rmxdev.braillex.ui.theme.White

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    navigateToSignup: () -> Unit,
    navigateToHelp: () -> Unit,
    backButton: () -> Unit
) {

    val systemUiController = rememberSystemUiController()
    val statusBarColor = White

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
            .background(White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { backButton() }, modifier = Modifier.size(70.dp)) {
                Icon(
                    painterResource(id = R.drawable.backbutton),
                    contentDescription = "Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.weight(0.3f))
            Icon(
                painterResource(id = R.drawable.logotipoclaro),
                contentDescription = "Logo",
                tint = Color.Unspecified,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(OffWhite)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Bienvenido a Braillex", fontSize = 35.sp, color = DarkBlack)
        Text(
            text = "Inicia sesión o crea una cuenta para convertir un archivo a audio gratis",
            fontSize = 25.sp,
            color = DarkBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navigateToLogin() },
            colors = buttonColors(containerColor = Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(48.dp)
            ) {
            Text(text = "Iniciar sesión", fontSize = 20.sp, color = White)
        }
        Button(
            onClick = { navigateToSignup() },
            colors = buttonColors(containerColor = White, contentColor = Blue),
            border = BorderStroke(2.dp, Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(48.dp)
            ) {
            Text(text = "Crear cuenta", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = { navigateToHelp() }) {
            Text(text = "¿Necesitas ayuda?", fontSize = 15.sp, color = BarColor)
        }
    }
}