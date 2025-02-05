package com.rmxdev.braillex.presenter.newFile

import android.net.Uri
import android.util.Log
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
import com.rmxdev.braillex.ui.theme.Blue
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.TextFieldColor
import com.rmxdev.braillex.ui.theme.White

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    navigateToInitial: () -> Unit,
    navigateToHelp: () -> Unit,
    navigateToUpload: (Uri, String) -> Unit,
    fileUri: Uri
) {

    var title by rememberSaveable { mutableStateOf("") }

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
            IconButton(onClick = { navigateToHelp() }, modifier = Modifier.size(70.dp)) {
                Icon(
                    painterResource(id = R.drawable.settingbutton),
                    contentDescription = "Ir a configuraciones",
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = "Escribe un título para el archivo",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = DarkBlack,
            modifier = Modifier.padding(24.dp)
        )
        Spacer(modifier = Modifier.weight(0.25f))
        TextField(
            value = title,
            onValueChange = {
                title = it
                Log.d("TitleScreen", "Nuevo título: $title")
            },
            label = { Text("Título") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
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
        Spacer(modifier = Modifier.weight(0.25f))
        Button(
            onClick = { navigateToUpload(fileUri, title) },
            colors = buttonColors(containerColor = Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(50.dp)
        ) {
            Text(text = "Siguiente", fontSize = 25.sp, color = White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.weight(4f))
    }
}