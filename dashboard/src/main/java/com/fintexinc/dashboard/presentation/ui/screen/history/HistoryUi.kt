package com.fintexinc.dashboard.presentation.ui.screen.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.presentation.ui.widget.ToolBar
import com.fintexinc.core.presentation.ui.widget.dialog.DeletePopup
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.CustomToast
import com.fintexinc.core.ui.components.TextButton
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R
import com.fintexinc.dashboard.presentation.ui.models.HistoryItemUi
import com.fintexinc.dashboard.presentation.viewmodel.HistoryViewModel

@Composable
fun HistoryUi(
    state: HistoryViewModel.State,
    onBackClicked: () -> Unit,
    onDetailsNavigate: () -> Unit,
    onEditModeToggle: () -> Unit,
    onItemSelectionToggle: (Int) -> Unit,
    onDeleteSelected: () -> Unit,
    onCancelEdit: () -> Unit,
    onUndoDelete: () -> Unit,
) {

    when (state) {
        is HistoryViewModel.State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is HistoryViewModel.State.Loaded -> Content(
            onBackClicked = onBackClicked,
            onDetailsNavigate = onDetailsNavigate,
            mainState = state.mainState,
            onEditModeToggle = onEditModeToggle,
            onItemSelectionToggle = onItemSelectionToggle,
            onDeleteSelected = onDeleteSelected,
            onCancelEdit = onCancelEdit,
            onUndoDelete = onUndoDelete,
        )
    }

}

@Composable
private fun Content(
    mainState: HistoryViewModel.MainState,
    onBackClicked: () -> Unit,
    onDetailsNavigate: () -> Unit,
    onEditModeToggle: () -> Unit,
    onItemSelectionToggle: (Int) -> Unit,
    onDeleteSelected: () -> Unit,
    onUndoDelete: () -> Unit,
    onCancelEdit: () -> Unit,
) {
    val showDeleteDialog = remember { mutableStateOf(false) }
    val showToast = remember { mutableStateOf(false) }
    val deletedCount = remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BackgroundSubdued)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ToolBar(
                text = if (mainState.isEditMode) {
                    stringResource(R.string.text_delete_edit_history)
                } else {
                    stringResource(R.string.text_entry_history_title, mainState.historyItem.size)
                },
                leftIcon = {
                    Icon(
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable { onBackClicked() },
                        painter = painterResource(id = com.fintexinc.core.R.drawable.ic_back_arrow),
                        contentDescription = stringResource(R.string.description_icon_back),
                        tint = Colors.BackgroundPrimary
                    )
                },
                rightIcon = {
                    if (!mainState.isEditMode) {
                        Icon(
                            modifier = Modifier
                                .wrapContentSize()
                                .clickable { onEditModeToggle() },
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = stringResource(R.string.description_icon_back),
                            tint = Colors.BackgroundPrimary
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            ) {
                itemsIndexed(mainState.historyItem) { index, boat ->
                    BoatItem(
                        boat = boat,
                        isFirst = index == 0,
                        isLast = index == mainState.historyItem.lastIndex,
                        isEditMode = mainState.isEditMode,
                        isSelected = mainState.selectedItems.contains(index),
                        onHistoryItemClicked = {
                            if (mainState.isEditMode) {
                                onItemSelectionToggle(index)
                            } else {
                                onDetailsNavigate()
                            }
                        },
                    )
                    if (index < mainState.historyItem.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp,
                        )
                    }
                }
            }

            if (mainState.isEditMode) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .background(Colors.Background)
                ) {
                    TextButton(
                        text = stringResource(
                            R.string.text_delete_selected_items,
                            mainState.selectedItems.size
                        ),
                        onClick = {
                            showDeleteDialog.value = true
                        },
                        color = Colors.Primary,
                        textStyle = FontStyles.TitleSmall,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        text = stringResource(R.string.text_cancel),
                        onClick = { onCancelEdit() },
                        color = Color.Transparent,
                        textColor = Colors.TextInteractive,
                        textStyle = FontStyles.TitleSmall,
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        AnimatedVisibility(
            visible = showToast.value,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            CustomToast(
                message = pluralStringResource(
                    R.plurals.text_entries_deleted_message,
                    deletedCount.intValue,
                    deletedCount.intValue
                ),
                onActionClick = {
                    showToast.value = false
                    onUndoDelete()
                },
                onDismiss = {
                    showToast.value = false
                },
                actionText = stringResource(R.string.text_undo),
            )
        }

        if (showDeleteDialog.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        showDeleteDialog.value = false
                    }
            ) {
                DeleteConfirmationDialog(
                    itemCount = mainState.selectedItems.size,
                    onConfirm = {
                        showDeleteDialog.value = false
                        deletedCount.intValue = mainState.selectedItems.size
                        onDeleteSelected()
                        showToast.value = true
                    },
                    onDismiss = {
                        showDeleteDialog.value = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    itemCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    DeletePopup(
        imageRes = com.fintexinc.core.R.drawable.ic_warning,
        imageContentDescription = "",
        title = stringResource(R.string.text_delete_selected_entries),
        text = stringResource(R.string.text_delete_confirmation_message, itemCount),
        onDeleteClick = onConfirm,
        onCancelClick = onDismiss,
        isSecondButtonVisible = false,
        approvedButtonText = R.string.text_delete_entries,
    )
}


@Composable
private fun BoatItem(
    boat: HistoryItemUi,
    isLast: Boolean = false,
    isFirst: Boolean = false,
    isEditMode: Boolean = false,
    isSelected: Boolean = false,
    onHistoryItemClicked: () -> Unit,
) {
    val shape = when {
        isFirst && isLast -> RoundedCornerShape(8.dp)
        isFirst -> RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        )

        isLast -> RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 8.dp,
            bottomEnd = 8.dp
        )

        else -> RoundedCornerShape(0.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.Background, shape = shape)
            .padding(16.dp)
            .clickableShape(
                shape = shape,
                onClick = { onHistoryItemClicked() }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = boat.title,
                style = FontStyles.BodyLarge,
                color = Colors.Text,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = boat.date,
                style = FontStyles.BodySmall,
                color = Colors.TextSubdued,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = boat.price,
                style = FontStyles.BodyMedium,
                color = Colors.Text,
            )
            Spacer(modifier = Modifier.width(8.dp))

            if (isEditMode) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (isSelected) Colors.Primary else Color.Transparent,
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = if (isSelected) Colors.Primary else Colors.IconSupplementary,
                            shape = CircleShape
                        )
                        .clickableShape(
                            shape = CircleShape,
                            onClick = { onHistoryItemClicked() },
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Colors.Background,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                Icon(
                    painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
                    contentDescription = stringResource(R.string.description_view_details),
                    tint = Colors.IconSupplementary,
                )
            }
        }
    }
}
