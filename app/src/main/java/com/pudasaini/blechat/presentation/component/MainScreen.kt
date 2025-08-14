package com.pudasaini.blechat.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pudasaini.blechat.presentation.BleIntent
import com.pudasaini.blechat.presentation.BleState
import java.nio.file.WatchEvent

@Composable
fun MainScreen(state: BleState, onIntent: (BleIntent) -> Unit){

    var pressedButton by remember { mutableStateOf("") }

    Column (Modifier
        .padding(16.dp)
        .fillMaxSize()
    ){
        OutlinedTextField(
            value = state.message,
            onValueChange = {onIntent(BleIntent.ChangeMessage(it))},
            label = { Text("You Message") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    pressedButton = "advertising"
                    onIntent(BleIntent.ToggleAdvertising)
                },
                modifier = Modifier.weight(1f)
                    .scale(if (pressedButton == "advertising")0.95f else 1f)
            ) {
                Text(if (state.isAdvertising) "Stop Advertising" else "Start Advertising")
            }

            OutlinedButton(
                onClick = {
                    onIntent(BleIntent.ToggleScanning)
                    pressedButton = "scanning"
                },
                modifier = Modifier.weight(1f)
                    .scale(if(pressedButton == "scanning") 0.95f else 1f)
            ) {
                Text(if (state.isScanning) "Stop Scanning" else "Start Scanning")
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Received Message:",
            style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ){
            items(state.receivedMessages){msg ->
                AnimatedMessage(msg, isSent = false)
            }
            if (state.message.isNotBlank()){
                item{
                    AnimatedMessage(state.message, isSent = true)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Scanning with Messages")
@Composable
fun PreviewScanningState(){
    MainScreen(
        state = BleState(
            message = "Scanning...",
            isAdvertising = false,
            isScanning = true,
            receivedMessages = listOf("Hi there!", "Welcome", "BLE is great!")
        ),
        onIntent = {}
    )
}

@Preview(showBackground = true, name = "Interactive Empty State")
@Composable
fun PreviewInteractiveEmptyState() {
    var message by remember { mutableStateOf("") }
    var isAdvertising by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }
    var receivedMessages by remember { mutableStateOf(listOf<String>()) }

    MainScreen(
        state = BleState(
            message = message,
            isAdvertising = isAdvertising,
            isScanning = isScanning,
            receivedMessages = receivedMessages
        ),
        onIntent = {
            when (it) {
                is BleIntent.ChangeMessage -> message = it.message
                BleIntent.ToggleAdvertising -> isAdvertising = !isAdvertising
                BleIntent.ToggleScanning -> isScanning = !isScanning
            }
        }
    )
}