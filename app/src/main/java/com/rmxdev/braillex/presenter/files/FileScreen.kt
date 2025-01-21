package com.rmxdev.braillex.presenter.files

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rmxdev.braillex.R
import com.rmxdev.braillex.ui.theme.Blue
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.White

@Composable
fun FileScreen(
    modifier: Modifier = Modifier,
    viewModel: FileViewModel = hiltViewModel(),
    navigateToInitial: () -> Unit,
    navigateToHelp: () -> Unit,
    navigateToPdfTitle: (String) -> Unit
) {
    val state = viewModel.fileState.collectAsState()

    // Definir el lanzador fuera del botón
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let { uri ->
                Log.d("UriCheck", "Uri: $uri")
                val encodedUri = Uri.encode(uri.toString())
                val safeUri = encodedUri.replace("/", "%2F")
                Log.d("UriParsedCheck", "Length: ${safeUri.length}, Uri: $safeUri")
                navigateToPdfTitle(safeUri)
            }
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
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Para convertir un archivo a audio presiona el botón azul",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = DarkBlack,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.weight(2f))

        IconButton(
            onClick = {
                Log.d("Button pressed", "Button pressed")
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/pdf"
                }
                launcher.launch(intent)
                Log.d("Intent Data", "Intent size: ${intent.extras?.size()}")
            },
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Blue)
        ) {
            Icon(
                painterResource(id = R.drawable.ic_add),
                contentDescription = "Agregar un nuevo archivo",
                tint = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .size(30.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

    }

    when (state.value) {
        is FileState.Error -> {
            val errorMessage = (state.value as FileState.Error).message
            Text(text = errorMessage)
        }

        FileState.Loading -> {
            CircularProgressIndicator()
        }

        is FileState.Success -> {
            val files = (state.value as FileState.Success).files
            LazyColumn {
                items(files.size) { file ->
                    Text(files[file].title)
                }
            }
        }

        else -> {}
    }
}
