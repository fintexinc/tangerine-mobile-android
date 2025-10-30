package com.fintexinc.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintexinc.core.data.model.BankingUI
import com.fintexinc.core.data.model.CustomUI
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.data.model.InvestmentUI
import com.fintexinc.core.data.model.LiabilityUI
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.dashboard.presentation.models.PerformanceResult
import com.fintexinc.dashboard.presentation.ui.mapper.toNameValue
import dagger.hilt.android.lifecycle.HiltViewModel
import domain.model.Account
import domain.model.Custom
import domain.model.Document
import domain.model.Liability
import domain.model.Transaction
import domain.usecase.account.GetAccountsUseCase
import domain.usecase.account.GetActivitiesUseCase
import domain.usecase.account.GetDocumentsUseCase
import domain.usecase.account.GetInvestmentPerformanceUseCase
import domain.usecase.base.BaseUseCase
import domain.usecase.networth.GetAssetsUseCase
import domain.usecase.networth.GetLiabilitiesUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getAssetsUseCase: GetAssetsUseCase,
    private val getLiabilitiesUseCase: GetLiabilitiesUseCase,
    private val getInvestmentPerformanceUseCase: GetInvestmentPerformanceUseCase,
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val getDocumentsUseCase: GetDocumentsUseCase,
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
        // Accounts
        val accountsResult = getAccountsUseCase()
        val accounts: List<Account> = when (accountsResult) {
            is BaseUseCase.Result.Success -> accountsResult.data
            is BaseUseCase.Result.Failure -> emptyList()
        }

        // Assets (Net worth)
        val assetsResult = getAssetsUseCase()
        val bankingAssetsRaw = when (assetsResult) {
            is BaseUseCase.Result.Success -> assetsResult.data.banking
            is BaseUseCase.Result.Failure -> emptyList()
        }
        val investmentAssetsRaw = when (assetsResult) {
            is BaseUseCase.Result.Success -> assetsResult.data.investment
            is BaseUseCase.Result.Failure -> emptyList()
        }
        val customAssetsRaw = when (assetsResult) {
            is BaseUseCase.Result.Success -> assetsResult.data.custom
            is BaseUseCase.Result.Failure -> emptyList()
        }

        // Liabilities
        val liabilitiesResult = getLiabilitiesUseCase()
        val liabilitiesRaw = when (liabilitiesResult) {
            is BaseUseCase.Result.Success -> liabilitiesResult.data
            is BaseUseCase.Result.Failure -> emptyList()
        }

        // Activities (Transactions)
        val activitiesResult = getActivitiesUseCase()
        val activities = when (activitiesResult) {
            is BaseUseCase.Result.Success -> activitiesResult.data.sortedByDescending { it.transactionDate }
                .take(ACTIVITIES_COUNT)
            is BaseUseCase.Result.Failure -> emptyList()
        }

        // Documents
        val documentsResult = getDocumentsUseCase()
        val documents: List<Document> = when (documentsResult) {
            is BaseUseCase.Result.Success -> documentsResult.data.sortedWith(
                compareByDescending<Document> { it.documentDate.year }
                    .thenByDescending { it.documentDate.month }
                    .thenByDescending { it.documentDate.day }
            ).take(ACTIVITIES_COUNT)
            is BaseUseCase.Result.Failure -> emptyList()
        }

        // Performance
        val performanceResult = getInvestmentPerformanceUseCase()
        val performance = when (performanceResult) {
            is BaseUseCase.Result.Failure -> PerformanceResult(error = performanceResult.throwable)
            is BaseUseCase.Result.Success -> PerformanceResult(performanceItems = performanceResult.data)
        }

        return State.Data(
            bankingAssets = bankingAssetsRaw.map {
                BankingUI(
                    it, it.toNameValue()
                )
            },
            investmentAssets = investmentAssetsRaw.map {
                InvestmentUI(
                    it, it.toNameValue()
                )
            },
            customAssets = customAssetsRaw.map {
                CustomUI(
                    it, it.toNameValue()
                )
            },
            liabilities = liabilitiesRaw.map {
                LiabilityUI(
                    it,
                    it.toNameValue()
                )
            },
            accounts = accounts,
            activities = activities,
            documents = documents,
            performance = performance
        )
    }

    fun getCustomAssetById(id: String): Custom? {
        return softDataCache?.customAssets?.firstOrNull { it.asset.id == id }?.asset
    }

    fun getLiabilityById(id: String): Liability? {
        return softDataCache?.liabilities?.firstOrNull { it.liability.id == id }?.liability
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

    fun onAddAsset(asset: Custom, isNew: Boolean) {
        viewModelScope.launch {
            val currentState = currentDataState()
            val updatedAssets = if (isNew) {
                currentState.customAssets.toMutableList().apply {
                    add(CustomUI(asset, asset.toNameValue()))
                }.toList()
            } else {
                currentState.customAssets.toMutableList().apply {
                    val index = indexOfFirst { it.asset.id == asset.id }
                    remove(get(index))
                    add(index, CustomUI(asset, asset.toNameValue()))
                }.toList()
            }
            _action.emit(Action.ShowObjectEditSuccess(asset.assetName))
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

    fun onAddLiability(liability: Liability, isNew: Boolean) {
        viewModelScope.launch {
            val currentState = currentDataState()
            val updatedLiabilities =
                if (isNew) {
                    currentState.liabilities.toMutableList().apply {
                        add(
                            LiabilityUI(
                                liability,
                                liability.toNameValue()
                            )
                        )
                    }.toList()
                } else {
                    currentState.liabilities.toMutableList().apply {
                        val index = indexOfFirst { it.liability.id == liability.id }
                        remove(get(index))
                        add(
                            index, LiabilityUI(
                                liability,
                                liability.toNameValue()
                            )
                        )
                    }.toList()
                }
            _action.emit(Action.ShowObjectEditSuccess(liability.liabilityName))
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
            documents = currentState.documents,
            performance = currentState.performance
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
            val documents: List<Document>,
            val performance: PerformanceResult
        ) : State()
    }

    sealed class Action {
        data class AddEditAsset(val assetId: String?) : Action()
        data class AddEditLiability(val liabilityId: String?) : Action()
        data class ShowObjectEditSuccess(val objectName: String) : Action()
    }

    companion object {
        private const val ACTIVITIES_COUNT = 5
    }
}