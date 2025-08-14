package com.pudasaini.blechat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pudasaini.blechat.domain.model.BleMessage
import com.pudasaini.blechat.domain.repository.BleRepository
import com.pudasaini.blechat.presentation.BleIntent
import com.pudasaini.blechat.presentation.BleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BleViewModel @Inject constructor(
    private val repository: BleRepository
): ViewModel()
{
    private val _state = MutableStateFlow(BleState())
    val state: StateFlow<BleState> = _state.asStateFlow()

    private var scanJob: Job? = null
    private var debounceJob: Job? = null

    fun handleIntent(intent: BleIntent){
        when(intent){
            is BleIntent.ChangeMessage -> onMessageChanged(intent.message)
            BleIntent.ToggleAdvertising -> toggleAdvertising()
            BleIntent.ToggleScanning -> toggleScanning()
        }
    }

    private fun onMessageChanged(new: String){
        _state.update { it.copy(message = new) }
        if (_state.value.isAdvertising){
            debounceJob?.cancel()
            debounceJob = viewModelScope.launch {
                delay(300)
                repository.updateAdvertisingMessage(BleMessage(new))
            }
        }
    }

    private fun toggleAdvertising(){
        val s = _state.value
        if (s.isAdvertising){
            repository.stopAdvertising()
            _state.update { it.copy(isAdvertising = false) }
        } else {
            if (s.message.isBlank()) return
            repository.startAdvertising(BleMessage(s.message))
            _state.update { it.copy(isAdvertising = true) }
        }
    }

    private fun toggleScanning(){
        if (_state.value.isScanning){
            repository.stopScanning()
            scanJob?.cancel()
            _state.update{it.copy(isScanning = false)}
        } else {
            scanJob = viewModelScope.launch {
                repository.startScanning().collect { msg ->
                    _state.update { st ->
                        if (msg.content in st.receivedMessages) st
                        else st.copy(receivedMessages = st.receivedMessages + msg.content)
                    }
                }
            }
            _state.update { it.copy(isScanning = true) }
        }
    }
}