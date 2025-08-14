package com.pudasaini.blechat.presentation

data class BleState(
    val message: String = "",
    val isAdvertising: Boolean = false,
    val isScanning: Boolean = false,
    val receivedMessages: List<String> = emptyList()
)