package com.rmxdev.braillex.presenter.email

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rmxdev.braillex.R
import com.rmxdev.braillex.ui.theme.BarColor
import com.rmxdev.braillex.ui.theme.Blue
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.TextFieldColor
import com.rmxdev.braillex.ui.theme.White

@Composable
fun EmailScreen(
    modifier: Modifier = Modifier,
    backButton: () -> Unit,
    navigateToSignup: (String) -> Unit,
    navigateToHelp: () -> Unit
) {

    var email by rememberSaveable { mutableStateOf("") }

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
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = "Ingresa tu email para crear una cuenta",
            fontSize = 20.sp,
            color = DarkBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 100.dp)
        )
        Spacer(modifier = Modifier.weight(0.5f))
        TextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .clip(CircleShape),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = TextFieldColor,
                focusedContainerColor = TextFieldColor,
                focusedTextColor = DarkBlack,
                unfocusedTextColor = DarkBlack,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 1
        )
        Button(
            onClick = { navigateToSignup(email) },
            colors = buttonColors(containerColor = Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(50.dp)
        ) {
            Text(text = "Siguiente", fontSize = 25.sp, color = White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.weight(2f))
        TextButton(onClick = { navigateToHelp() }) {
            Text(text = "¿Necesitas ayuda?", fontSize = 15.sp, color = BarColor)
        }
    }
}