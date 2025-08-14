package com.pudasaini.blechat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.pudasaini.blechat.presentation.BleIntent
import com.pudasaini.blechat.presentation.BleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeBleViewModel: ViewModel() {
    private val _state = MutableStateFlow(
        BleState(
            message = "Hello BLE",
            isAdvertising = false,
            isScanning = true,
            receivedMessages = listOf("Hello there!", "Welcome!", "This is the world of BLE!")
        )
    )
    val state: StateFlow<BleState> = _state

    fun handleIntent(intent: BleIntent){
        when(intent){
            is BleIntent.ChangeMessage ->
                _state.value = _state.value.copy(message = intent.message)

            BleIntent.ToggleAdvertising ->
                _state.value = _state.value.copy(isAdvertising = !_state.value.isAdvertising)

            BleIntent.ToggleScanning ->
                _state.value = _state.value.copy(isScanning = !_state.value.isScanning)
        }
    }
}