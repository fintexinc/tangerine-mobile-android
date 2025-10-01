package com.fintexinc.dashboard.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintexinc.core.data.model.BankingUI
import com.fintexinc.core.data.model.CustomUI
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.data.model.InvestmentUI
import com.fintexinc.core.data.model.LiabilityUI
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.gateway.NetWorthGateway
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.Custom
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.Liability
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.dashboard.presentation.ui.mapper.toNameValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
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

    private val _action = MutableSharedFlow<Action>()
    val action: MutableSharedFlow<Action>
        get() = _action

    private var softDataCache: State.Data? = null


    init {
        viewModelScope.launch {
            _state.value = getData().also {
                softDataCache = it
            }
        }
    }

    private suspend fun getData(): State.Data {
        val accounts = accountGateway.getAccounts()
        val assets = netWorthGateway.getAssets()
        val liabilities = netWorthGateway.getLiabilities()
        val activities = accountGateway.getActivities().sortedByDescending { it.transactionDate }
            .take(ACTIVITIES_COUNT)
        val documents = accountGateway.getDocuments().sortedWith(
            compareBy({ it.documentDate.year }, { it.documentDate.month }, { it.documentDate.day })
        ).take(ACTIVITIES_COUNT)

        return State.Data(
            bankingAssets = assets.banking.map {
                BankingUI(
                    it, it.toNameValue()
                )
            }, investmentAssets = assets.investment.map {
                InvestmentUI(
                    it, it.toNameValue()
                )
            }, customAssets = assets.custom.map {
                CustomUI(
                    it, it.toNameValue()
                )
            }, liabilities = liabilities.map {
                LiabilityUI(
                    it,
                    it.toNameValue(context.getString(com.fintexinc.dashboard.R.string.text_effective_on))
                )
            }, accounts = accounts, activities = activities, documents = documents
        )
    }

    fun getCustomAssetById(id: String): Custom? {
        return softDataCache?.customAssets?.firstOrNull { it.asset.id == id }?.asset
    }

    fun getLiabilityById(id: String): Liability? {
        return softDataCache?.liabilities?.firstOrNull { it.liability.id == id }?.liability
    }

    fun onPlatformClicked() {
        // Handle platform click event
    }

    fun onAddAssetClicked(dataPoint: DataPoint?) = viewModelScope.launch {
        if (dataPoint != null && currentDataState().customAssets.firstOrNull {
                it.asset.id == dataPoint.id
            } == null
        ) {
            return@launch
        }
        _action.emit(
            Action.AddEditAsset(dataPoint?.id)
        )
    }

    fun onAddAsset(asset: Custom) {
        viewModelScope.launch {
            val currentState = currentDataState()
            val updatedAssets =
                if (currentState.customAssets.firstOrNull { it.asset.id == asset.id } != null) {
                    currentState.customAssets.toMutableList().apply {
                        val index = indexOfFirst { it.asset.id == asset.id }
                        remove(get(index))
                        add(index, CustomUI(asset, asset.toNameValue()))
                    }
                } else {
                    currentState.customAssets.toMutableList().apply {
                        add(CustomUI(asset, asset.toNameValue()))
                    }
                }
            _state.value = currentState.copy(customAssets = updatedAssets)
            softDataCache = _state.value as? State.Data
        }
    }

    fun onDeleteAsset(asset: Custom) {
        viewModelScope.launch {
            val currentState = currentDataState()
            val updatedAssets = currentState.customAssets.toMutableList().apply {
                removeAll { it.asset.id == asset.id }
            }
            _state.value = currentState.copy(customAssets = updatedAssets)
            softDataCache = _state.value as? State.Data
        }
    }

    fun onAddLiabilityClicked(dataPoint: DataPoint?) = viewModelScope.launch {
        _action.emit(
            Action.AddEditLiability(dataPoint?.id)
        )
    }

    fun onAddLiability(liability: Liability) {
        viewModelScope.launch {
            val currentState = currentDataState()
            val updatedLiabilities =
                if (currentState.liabilities.firstOrNull { it.liability.id == liability.id } != null) {
                    currentState.liabilities.toMutableList().apply {
                        val index = indexOfFirst { it.liability.id == liability.id }
                        remove(get(index))
                        add(
                            index, LiabilityUI(
                                liability,
                                liability.toNameValue(context.getString(com.fintexinc.dashboard.R.string.text_effective_on))
                            )
                        )
                    }
                } else {
                    currentState.liabilities.toMutableList().apply {
                        add(
                            LiabilityUI(
                                liability,
                                liability.toNameValue(context.getString(com.fintexinc.dashboard.R.string.text_effective_on))
                            )
                        )
                    }
                }
            _state.value = currentState.copy(liabilities = updatedLiabilities)
            softDataCache = _state.value as? State.Data
        }
    }

    fun onDeleteLiability(liability: Liability) {
        viewModelScope.launch {
            val currentState = currentDataState()
            val updatedLiabilities = currentState.liabilities.toMutableList().apply {
                removeAll { it.liability.id == liability.id }
            }
            _state.value = currentState.copy(liabilities = updatedLiabilities)
            softDataCache = _state.value as? State.Data
        }
    }

    fun updateCheckedStates(
        assetStates: List<NameValueChecked>,
        liabilityStates: List<NameValueChecked>,
    ) = viewModelScope.launch {
        val currentState = currentDataState()
        _state.value = State.Data(
            liabilities = currentState.liabilities.map { liabilityUI ->
                liabilityUI.copy(checkedState = liabilityStates.find { state -> state.id == liabilityUI.liability.id }
                    ?: liabilityUI.checkedState)
            },
            bankingAssets = currentState.bankingAssets.map { bankingUI ->
                bankingUI.copy(checkedState = assetStates.find { state -> state.id == bankingUI.asset.id }
                    ?: bankingUI.checkedState)
            },
            investmentAssets = currentState.investmentAssets.map { investmentUI ->
                investmentUI.copy(checkedState = assetStates.find { state -> state.id == investmentUI.asset.id }
                    ?: investmentUI.checkedState)
            },
            customAssets = currentState.customAssets.map { customUI ->
                customUI.copy(checkedState = assetStates.find { state -> state.id == customUI.asset.id }
                    ?: customUI.checkedState)
            },
            accounts = currentState.accounts,
            activities = currentState.activities,
            documents = currentState.documents
        )
    }

    private suspend fun currentDataState(): State.Data {
        return softDataCache ?: getData().also {
            softDataCache = it
        }
    }

    sealed class State {
        object Loading : State()
        data class Data(
            val bankingAssets: List<BankingUI>,
            val investmentAssets: List<InvestmentUI>,
            val customAssets: List<CustomUI>,
            val liabilities: List<LiabilityUI>,
            val accounts: List<Account>,
            val activities: List<Transaction>,
            val documents: List<Document>
        ) : State()
    }

    sealed class Action {
        data class AddEditAsset(val assetId: String?) : Action()
        data class AddEditLiability(val liabilityId: String?) : Action()
    }

    companion object {
        private const val ACTIVITIES_COUNT = 5
    }
}