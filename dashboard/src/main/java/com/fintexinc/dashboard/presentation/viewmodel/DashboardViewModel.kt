package com.fintexinc.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.gateway.NetWorthGateway
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.dashboard.presentation.ui.mapper.toNameValue
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val accountGateway: AccountGateway,
    private val netWorthGateway: NetWorthGateway,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    fun getData() = viewModelScope.launch {
        val accounts = accountGateway.getAccounts()
        val assets = netWorthGateway.getAssets()
        val liabilities = netWorthGateway.getLiabilities()
        val activities = accountGateway.getActivities()
        val documents = accountGateway.getDocuments()
        _state.value = State.Data(
            assets = mutableListOf<NameValueChecked>().apply {
                addAll(assets.investment.map { it.toNameValue() })
                addAll(assets.custom.map { it.toNameValue() })
            },
            liabilities = liabilities.map { it.toNameValue() },
            accounts = accounts,
            activities = activities,
            documents = documents
        )
    }

    fun onPlatformClicked() {
        // Handle platform click event
    }

    fun updateCheckedStates(
        assets: List<NameValueChecked>,
        checkedStates: List<Boolean>,
        liabilities: List<NameValueChecked>,
        checkedLiabilityStates: List<Boolean>
    ) {
        _state.value = State.Data(
            liabilities = liabilities.mapIndexed { index, nameValueChecked ->
                nameValueChecked.copy(
                    isChecked = checkedLiabilityStates.getOrNull(index) ?: false
                )
            },
            assets = assets.mapIndexed { index, nameValueChecked ->
                nameValueChecked.copy(
                    isChecked = checkedStates.getOrNull(
                        index
                    ) ?: false
                )
            },
            accounts = (_state.value as? State.Data)?.accounts ?: emptyList(),
            activities = (_state.value as? State.Data)?.activities ?: emptyList(),
            documents = (_state.value as? State.Data)?.documents ?: emptyList()
        )
    }

    sealed class State {
        object Loading : State()
        data class Data(
            val assets: List<NameValueChecked>,
            val liabilities: List<NameValueChecked>,
            val accounts: List<Account>,
            val activities: List<Transaction>,
            val documents: List<Document>
        ) : State()
    }
}