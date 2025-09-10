package com.tangerine.account.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintexinc.core.data.model.NameValue
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.Transaction
import com.tangerine.account.presentation.ui.AccountTab
import com.tangerine.account.presentation.viewmodel.AccountViewModel.State.Activities
import com.tangerine.account.presentation.viewmodel.AccountViewModel.State.Documents
import com.tangerine.account.presentation.viewmodel.AccountViewModel.State.Positions
import com.tangerine.account.presentation.viewmodel.AccountViewModel.State.Summary
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AccountViewModel.Factory::class)
class AccountViewModel @AssistedInject constructor(
    @Assisted private val id: String,
    private val accountGateway: AccountGateway
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: String): AccountViewModel
    }

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    fun getData() = viewModelScope.launch {
        _state.value = Summary(accountGateway.getAccountById(id))
    }

    fun onTabChanged(tab: AccountTab) = viewModelScope.launch {
        when (tab) {
            AccountTab.SUMMARY -> {
                _state.value = Summary(accountGateway.getAccountById(id))
            }

            AccountTab.ACTIVITY -> {
                _state.value = Activities(accountGateway.getActivities())
            }

            AccountTab.POSITIONS -> {
                _state.value = Positions(
                    listOf(
                        NameValue(
                            name = "Management Expense Ratio (MER)",
                            value = "1.2%"
                        ),
                        NameValue(
                            name = "Distributions",
                            value = "Annually, December"
                        ),
                        NameValue(
                            name = "Account type",
                            value = "TFSA"
                        ),
                        NameValue(
                            name = "Units",
                            value = "54"
                        ),
                        NameValue(
                            name = "Total Market Value",
                            value = "$100,00-$249,999"
                        ),
                        NameValue(
                            name = "Total Book Value",
                            value = "$1000000"
                        ),
                        NameValue(
                            name = "Gain / Loss",
                            value = "+$1,000,000",
                            valueColor = Color(0xFF136F68)
                        )
                    )
                )
            }

            AccountTab.DOCUMENTS -> {
                _state.value = Documents(accountGateway.getDocumentsByAccountId(id))
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Summary(val data: Account) : State()

        // TODO: Figure out what to use here
        data class Positions(val data: List<NameValue>) : State()
        data class Activities(val data: List<Transaction>) : State()
        data class Documents(val data: List<Document>) : State()
    }
}