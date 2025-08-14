package com.pudasaini.blechat.domain.repository

import com.pudasaini.blechat.domain.model.BleMessage
import kotlinx.coroutines.flow.Flow

interface BleRepository {
    fun startAdvertising(message: BleMessage)
    fun updateAdvertisingMessage(message: BleMessage)
    fun stopAdvertising()
    fun startScanning(): Flow<BleMessage>
    fun stopScanning()
}