package com.rmxdev.braillex.presenter.files

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.rmxdev.braillex.R
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.ui.theme.Blue
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.OffWhite
import com.rmxdev.braillex.ui.theme.White

@Composable
fun FileScreen(
    modifier: Modifier = Modifier,
    viewModel: FileViewModel = hiltViewModel(),
    navigateToInitial: () -> Unit,
    navigateToHelp: () -> Unit,
    onFileSelected: (Uri) -> Unit,
    navigateToReproductor: (String) -> Unit
) {
    val selectedFile by viewModel.selectedFile.collectAsState()
    var isFileSelected by remember { mutableStateOf(false) }
    val files by viewModel.files.collectAsState()

    // Definir el lanzador fuera del botón
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                viewModel.selectFile(it)
                isFileSelected = true // Marcar como seleccionado
            }
        }

    LaunchedEffect(isFileSelected) {
        if (isFileSelected) {
            selectedFile?.let {
                onFileSelected(it)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White),
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

        LazyColumn(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(files) { file ->
                FileItemRow(file = file, onClick = { navigateToReproductor(file.id)
                    Log.d("FileScreen", "File ID: $it")})
            }
        }

        Text(
            text = "Para convertir un archivo a audio presiona el botón azul",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = DarkBlack,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.weight(2f))

        IconButton(
            onClick = { launcher.launch(arrayOf("application/pdf")) },
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
        Text("Subir Archivo", fontSize = 20.sp, color = DarkBlack, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun FileItemRow(file: PdfFile, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(file.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = OffWhite)
    ) {
        Text(file.title, modifier = Modifier.padding(16.dp), color = DarkBlack)
    }
}
