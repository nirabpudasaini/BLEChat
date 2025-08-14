package com.pudasaini.blechat.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import com.pudasaini.blechat.domain.model.BleMessage
import com.pudasaini.blechat.domain.repository.BleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BleRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : BleRepository{

    private val bluetoothAdapter: BluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private val advertiser: BluetoothLeAdvertiser? = bluetoothAdapter.bluetoothLeAdvertiser
    private val scanner: BluetoothLeScanner? = bluetoothAdapter.bluetoothLeScanner

    private val manufacturerId = 0x1234
    private var advertiseCallback: AdvertiseCallback? = null
    private var scanCallback: ScanCallback? = null
    private var lastBytes: ByteArray? = null

    @SuppressLint("MissingPermission")
    override fun startAdvertising(message: BleMessage) {
        val bytes = encode(message)
        lastBytes = bytes

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(false)
            .build()

        val data = AdvertiseData.Builder()
            .addManufacturerData(manufacturerId, bytes)
            .build()

        val cb = object : AdvertiseCallback(){}
        advertiseCallback = cb
        advertiser?.startAdvertising(settings, data, cb)

    }

    override fun updateAdvertisingMessage(message: BleMessage) {
        val bytes = encode(message)
        if (bytes.contentEquals(lastBytes)) return
        stopAdvertising()
        startAdvertising(message)
    }

    @SuppressLint("MissingPermission")
    override fun stopAdvertising() {
        advertiseCallback?.let { advertiser?.stopAdvertising(it) }
        advertiseCallback = null
    }

    @SuppressLint("MissingPermission")
    override fun startScanning(): Flow<BleMessage> = callbackFlow{
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        val cb = object : ScanCallback(){
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.scanRecord?.getManufacturerSpecificData(manufacturerId)?.let { bytes ->
                    trySend(BleMessage(String(bytes, Charset.defaultCharset())))
                }
            }
        }
        scanCallback = cb
        scanner?.startScan(null, settings, cb)

        awaitClose{
            scanner?.stopScan(cb)
            if (scanCallback === cb) scanCallback = null
        }
    }

    @SuppressLint("MissingPermission")
    override fun stopScanning() {
        scanCallback?.let { scanner?.stopScan(it) }
        scanCallback = null
    }

    private fun encode(m: BleMessage): ByteArray =
        m.content.take(MAX_LEN).toByteArray(Charset.defaultCharset())

    companion object{
        private const val MAX_LEN = 20
    }

}