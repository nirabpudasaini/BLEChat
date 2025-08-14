package com.pudasaini.blechat.presentation

sealed class BleIntent {
    data class ChangeMessage(val message: String): BleIntent()
    object ToggleAdvertising: BleIntent()
    object ToggleScanning: BleIntent()
}