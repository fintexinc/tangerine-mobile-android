package com.fintexinc.dashboard.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.gateway.NetWorthGateway
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.Custom
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.Liability
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.dashboard.presentation.ui.mapper.toNameValue
import com.tangerine.charts.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountGateway: AccountGateway,
    private val netWorthGateway: NetWorthGateway,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    private var softDataCache: State.Data? = null

    private suspend fun getData(): State.Data {
        val accounts = accountGateway.getAccounts()
        val assets = netWorthGateway.getAssets()
        val liabilities = netWorthGateway.getLiabilities()
        val activities = accountGateway.getActivities()
            .sortedByDescending { it.transactionDate }
            .take(3)

        val documents = accountGateway
            .getDocuments()
            .take(3)
        return State.Data(
            assets = mutableListOf<NameValueChecked>().apply {
                addAll(assets.investment.map { it.toNameValue() })
                addAll(assets.banking.map { it.toNameValue() })
                addAll(assets.custom.map { it.toNameValue() })
            },
            liabilities = liabilities.map {
                it.toNameValue(
                    context.getString(com.fintexinc.dashboard.R.string.text_effective_on)
                )
            },
            accounts = accounts,
            activities = activities,
            documents = documents
        )
    }

    fun loadData() = viewModelScope.launch {
        _state.value = getData().also {
            softDataCache = it
        }
    }

    fun onPlatformClicked() {
        // Handle platform click event
    }

    fun onAddAssetClicked() {
        _state.value = State.AddAsset
    }

    fun onAddAsset(asset: Custom) {
        viewModelScope.launch {
            val currentState = softDataCache ?: getData()
            val updatedAssets = currentState.assets.toMutableList().apply {
                add(asset.toNameValue())
            }
            _state.value = currentState.copy(assets = updatedAssets)
            softDataCache = _state.value as? State.Data
        }
    }

    fun onAddLiabilityClicked() {
        _state.value = State.AddLiability
    }

    fun onAddLiability(liability: Liability) {
        viewModelScope.launch {
            val currentState = softDataCache ?: getData()
            val updatedLiabilities = currentState.liabilities.toMutableList().apply {
                add(liability.toNameValue(context.getString(com.fintexinc.dashboard.R.string.text_effective_on)))
            }
            _state.value = currentState.copy(liabilities = updatedLiabilities)
            softDataCache = _state.value as? State.Data
        }
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

        object AddAsset : State()
        object AddLiability : State()
    }
}