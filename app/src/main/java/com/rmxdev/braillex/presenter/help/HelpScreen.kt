package com.rmxdev.braillex.presenter.help

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rmxdev.braillex.R
import com.rmxdev.braillex.ui.theme.DarkBlack
import com.rmxdev.braillex.ui.theme.OffWhite
import com.rmxdev.braillex.ui.theme.White

@Composable
fun HelpScreen(
    modifier: Modifier = Modifier,
    viewModel: HelpViewModel = hiltViewModel(),
    backButton: () -> Unit,
    navigateToSupport: () -> Unit,
    navigateToInitial: () -> Unit
) {

    val deleteAccountState by viewModel.deleteAccountState.collectAsState()
    val password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

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
        Spacer(modifier = Modifier.weight(2f))
        Button(
            onClick = { navigateToSupport() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = buttonColors(containerColor = OffWhite, contentColor = DarkBlack),
        ) {
            Text("Soporte Técnico", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = { viewModel.logout { navigateToInitial() } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = buttonColors(containerColor = OffWhite, contentColor = Color.Red),
        ) {
            Text("Cerrar sesión", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.weight(1f))
        DeleteAccountButton(viewModel)
        Spacer(modifier = Modifier.weight(1f))
        when (deleteAccountState) {
            is DeleteAccountState.Loading -> {
                CircularProgressIndicator()
            }

            is DeleteAccountState.Success -> {
                navigateToInitial()
                viewModel.resetState()
            }

            is DeleteAccountState.Error -> {
                LaunchedEffect(deleteAccountState) {
                    Toast.makeText(
                        context, "Error al eliminar la cuenta", Toast.LENGTH_LONG
                    ).show()
                }
                Log.e("Error", deleteAccountState.toString())
            }

            else -> {}
        }
    }

}