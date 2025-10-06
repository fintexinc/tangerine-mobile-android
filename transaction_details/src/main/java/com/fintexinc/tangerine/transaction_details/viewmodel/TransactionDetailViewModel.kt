package com.fintexinc.tangerine.transaction_details.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.core.ui.models.DataSectionItemUi
import com.fintexinc.tangerine.transaction_details.R
import com.fintexinc.tangerine.transaction_details.models.DocumentUi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountGateway: AccountGateway,
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(
        State.Loading
    )
    val state: StateFlow<State>
        get() = _state.asStateFlow()

    fun loadTransaction(documentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO() need to pass a valid ID to get a model with data that can be filled ui
            val document = accountGateway.getDocumentsById("DOC-001")
            val mockData = DocumentUi(
                id = "1",
                amount = "$1,000.00",
                description = "Deposit to TFSA ***7912",
                status = "Completed",
                statusColor = Colors.Text,
                sentFrom = "CHQ ***2003",
                sentTo = "Jack TFSA ***7912",
                category = "Investment",
                transactionDate = "August 14, 2025",
                note = null
            )
            _state.value = State.Data(
                document = mockData,
                documentItems = createDataSectionItems(mockData)
            )
        }
    }

    fun onSaveNote(note: String) {
        val currentState = _state.value
        if (currentState is State.Data) {
            val updatedTransaction = currentState.document.copy(
                note = note.ifBlank { null }
            )

            _state.value = State.Data(
                document = updatedTransaction,
                documentItems = createDataSectionItems(updatedTransaction),
            )
        }
    }

    private fun createDataSectionItems(document: DocumentUi): List<DataSectionItemUi> {
        return listOf(
            DataSectionItemUi(
                label = context.getString(R.string.text_status),
                value = document.status,
                valueColor = document.statusColor,
                valueStyle = FontStyles.BodyLarge,
            ),
            DataSectionItemUi(
                label = context.getString(R.string.text_sent_from),
                value = document.sentFrom,
                valueColor = Colors.Text,
                valueStyle = FontStyles.BodyLarge,
            ),
            DataSectionItemUi(
                label = context.getString(R.string.text_sent_to),
                value = document.sentTo,
                valueColor = Colors.Text,
                valueStyle = FontStyles.BodyLarge,
            ),
            DataSectionItemUi(
                label = context.getString(R.string.text_category),
                value = document.category,
                valueColor = Colors.Text,
                valueStyle = FontStyles.BodyLarge,
            ),
            DataSectionItemUi(
                label = context.getString(R.string.text_transaction_date),
                value = document.transactionDate,
                valueColor = Colors.Text,
                valueStyle = FontStyles.BodyLarge,
            )
        )
    }

    sealed class State {
        object Loading : State()
        data class Data(
            val document: DocumentUi,
            val documentItems: List<DataSectionItemUi>
        ) : State()
    }
}