package com.fintexinc.tangerine.transaction_details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.presentation.ui.widget.ToolBar
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.UniversalDataSection
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.core.ui.models.DataSectionItemUi
import com.fintexinc.tangerine.transaction_details.R
import com.fintexinc.tangerine.transaction_details.viewmodel.TransactionDetailViewModel

@Composable
fun TransactionDetailUi(
    state: TransactionDetailViewModel.State,
    onBackClick: () -> Unit,
) {

    when (state) {
        is TransactionDetailViewModel.State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

        is TransactionDetailViewModel.State.Data -> {
            Content(
                state = state,
                onBackClick = onBackClick,
            )
        }
    }
}

@Composable
private fun Content(
    state: TransactionDetailViewModel.State,
    onBackClick: () -> Unit,
) {
    // TODO() mock data
    val transactionItems = listOf(
        DataSectionItemUi(
            label = "Status",
            value = "Completed",
            valueColor = Colors.Text,
            valueStyle = FontStyles.BodyLarge
        ),
        DataSectionItemUi(
            label = "Sent from",
            value = "CHQ ***2003",
            valueColor = Colors.Text,
            valueStyle = FontStyles.BodyLarge
        ),
        DataSectionItemUi(
            label = "Sent to",
            value = "Jack TFSA ***7912",
            valueColor = Colors.Text,
            valueStyle = FontStyles.BodyLarge
        ),
        DataSectionItemUi(
            label = "Category",
            value = "Investment",
            valueColor = Colors.Text,
            valueStyle = FontStyles.BodyLarge
        ),
        DataSectionItemUi(
            label = "Transaction date",
            value = "August 4, 2025",
            valueColor = Colors.Text,
            valueStyle = FontStyles.BodyLarge
        )
    )

    var showBottomSheet by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BackgroundSubdued)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ToolBar(
            text = stringResource(R.string.title_details),
            leftIcon = {
                Icon(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable { onBackClick() },
                    painter = painterResource(com.fintexinc.core.R.drawable.ic_back_arrow),
                    contentDescription = stringResource(R.string.description_icon_navigate_back),
                    tint = Colors.Primary,
                )
            },
            rightIcon = {
                Icon(
                    painter = painterResource(com.fintexinc.core.R.drawable.icon_dots),
                    contentDescription = stringResource(R.string.description_icon_navigate_edit),
                    tint = Colors.Primary,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {}
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "$1,000.00",
            color = Colors.Primary,
            style = FontStyles.DisplaySmall,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Deposit to TFSA ***7912",
            color = Colors.Text,
            style = FontStyles.BodyLargeBold,
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniversalDataSection(
            title = null,
            items = transactionItems
        )

        Spacer(modifier = Modifier.height(24.dp))

        NoteUI(
            addNoteClick = {
                showBottomSheet = true
            },
        )
    }
    if (showBottomSheet) {
        AddNoteBottomSheet(
            onDismiss = { showBottomSheet = false },
            onSave = { note ->
                showBottomSheet = false
            }
        )
    }
}

@Composable
private fun NoteUI(
    addNoteClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = Colors.Background,
                shape = RoundedCornerShape(16.dp)
            )
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val dashWidth = 8.dp.toPx()
                val dashGap = 6.dp.toPx()
                val cornerRadius = 16.dp.toPx()

                val pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(dashWidth, dashGap),
                    phase = 0f
                )

                drawRoundRect(
                    color = Color.Gray.copy(alpha = 0.3f),
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(size.width - strokeWidth, size.height - strokeWidth),
                    cornerRadius = CornerRadius(cornerRadius),
                    style = Stroke(width = strokeWidth, pathEffect = pathEffect)
                )
            }
            .clickableShape(
                shape = RoundedCornerShape(16.dp),
                onClick = addNoteClick
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 52.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_pictogram),
                contentDescription = stringResource(R.string.description_icon_pictogram),
                tint = Color.Unspecified,
                modifier = Modifier.size(64.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.text_aad_note),
                style = FontStyles.BodySmall,
                color = Colors.Text,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteBottomSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var noteText by remember { mutableStateOf("") }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = { onDismiss() },
        shape = RoundedCornerShape(16.dp),
        containerColor = Colors.Background,
        dragHandle = null,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Colors.TextInteractive
                    ),
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        text = stringResource(R.string.text_cancel),
                        style = FontStyles.BodyLarge
                    )
                }

                Text(
                    text = stringResource(R.string.text_aad_note),
                    style = FontStyles.TitleSmall,
                    color = Colors.Text,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            HorizontalDivider(color = Colors.BorderSubdued)

            TextField(
                value = noteText,
                onValueChange = { noteText = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.text_note),
                        color = Colors.BorderSubdued,
                        style = FontStyles.BodyMedium
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Colors.Text
                ),
                textStyle = FontStyles.BodyMedium.copy(color = Colors.Text),
                maxLines = 8
            )
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Colors.BorderSubdued,
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1.0f))

            com.fintexinc.core.ui.components.TextButton(
                text = stringResource(R.string.text_save_text),
                onClick = { onSave(noteText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = Colors.Primary,
            )
        }
    }
}
