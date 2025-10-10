package com.fintexinc.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.fintexinc.dashboard.presentation.ui.models.HistoryItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList

@HiltViewModel
class HistoryViewModel @Inject constructor(

) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State>
        get() = _state.asStateFlow()


    init {
        loadHistory()
    }

    private fun loadHistory() {
        val mockBoats = listOf(
            HistoryItemUi("Boat", "MAY 2, 2024", "$300,000"),
            HistoryItemUi("Boat", "FEB 2, 2024", "$400,000"),
            HistoryItemUi("Boat", "OCT 2, 2021", "$500,000")
        )

        _state.value = State.Loaded(
            MainState(
                historyItem = mockBoats.toImmutableList(),
                isEditMode = false,
                selectedItems = emptySet()
            )
        )
    }

    fun toggleEditMode() {
        val currentState = (_state.value as? State.Loaded)?.mainState ?: return
        _state.value = State.Loaded(
            currentState.copy(
                isEditMode = !currentState.isEditMode,
                selectedItems = emptySet()
            )
        )
    }

    fun toggleItemSelection(index: Int) {
        val currentState = (_state.value as? State.Loaded)?.mainState ?: return
        val newSelectedItems = if (currentState.selectedItems.contains(index)) {
            currentState.selectedItems - index
        } else {
            currentState.selectedItems + index
        }
        _state.value = State.Loaded(
            currentState.copy(selectedItems = newSelectedItems)
        )
    }

    fun deleteSelectedItems() {
        val currentState = (_state.value as? State.Loaded)?.mainState ?: return
        val newList = currentState.historyItem.filterIndexed { index, _ ->
            !currentState.selectedItems.contains(index)
        }.toImmutableList()

        _state.value = State.Loaded(
            MainState(
                historyItem = newList,
                isEditMode = false,
                selectedItems = emptySet()
            )
        )
    }

    fun cancelEdit() {
        val currentState = (_state.value as? State.Loaded)?.mainState ?: return
        _state.value = State.Loaded(
            currentState.copy(
                isEditMode = false,
                selectedItems = emptySet()
            )
        )
    }

    sealed class State {
        object Loading : State()
        data class Loaded(val mainState: MainState) : State()
    }

    data class MainState(
        val historyItem: ImmutableList<HistoryItemUi>,
        val isEditMode: Boolean = false,
        val selectedItems: Set<Int> = emptySet()
    )
}