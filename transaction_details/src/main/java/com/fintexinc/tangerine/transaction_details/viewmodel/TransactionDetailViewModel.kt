package com.fintexinc.tangerine.transaction_details.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(

) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(
        State.Data()
    )
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    sealed class State {
        object Loading : State()
        data class Data(val data: Any? = null) : State()
    }
}