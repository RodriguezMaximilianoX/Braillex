package com.rmxdev.braillex.presenter.support

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rmxdev.braillex.R
import com.rmxdev.braillex.ui.theme.Blue
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.White

@Composable
fun SupportScreen(
    modifier: Modifier = Modifier,
    backButton: () -> Unit,
) {
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
            IconButton(onClick = { backButton() }, modifier = Modifier.size(70.dp)) {
                Icon(
                    painterResource(id = R.drawable.backbutton),
                    contentDescription = "Volver al inicio",
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Configuración", fontSize = 20.sp, color = DarkBlack)
            Spacer(modifier = Modifier.weight(2f))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "¿Necesitas ayuda?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlack,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            "Envianos un mensaje detallando el problema a nuestro email",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 50.dp)
        )
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            colors = buttonColors(containerColor = Blue, contentColor = White)
        ) {
            Text(text = "hola@braillex.com.ar", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.weight(2f))
    }
}