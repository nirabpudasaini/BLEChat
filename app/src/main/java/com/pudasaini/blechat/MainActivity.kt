package com.pudasaini.blechat

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.pudasaini.blechat.presentation.component.MainScreen
import com.pudasaini.blechat.presentation.viewmodel.BleViewModel
import com.pudasaini.blechat.ui.theme.BLEChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionLauncher = registerForActivityResult (
        ActivityResultContracts.RequestMultiplePermissions()
    ){  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request runtime permissions (idempotent)
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,// pre-Android 12
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADVERTISE
        ))

        //enableEdgeToEdge()
        setContent {
            val vm: BleViewModel = hiltViewModel()
            val state = vm.state.collectAsState()
            BLEChatTheme {
                MainScreen(
                    state = state.value,
                    onIntent = vm::handleIntent
                )
            }
        }
    }
}