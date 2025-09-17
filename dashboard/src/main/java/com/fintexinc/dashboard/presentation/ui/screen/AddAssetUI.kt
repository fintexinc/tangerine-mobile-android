package com.fintexinc.dashboard.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.utils.date.DateUtils
import com.fintexinc.core.domain.model.AssetType
import com.fintexinc.core.domain.model.Custom
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.presentation.ui.widget.add.AddItemSelection
import com.fintexinc.core.presentation.ui.widget.add.AddItemText
import com.fintexinc.core.presentation.ui.widget.add.DateSelectionType
import com.fintexinc.core.presentation.ui.widget.add.ItemTypeSelection
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R

@Composable
fun AddAssetUI(
    onSaveAssetClick: (asset: Custom) -> Unit,
    onBackButtonFromExternalScreenClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.Background)
    ) {
        val showAssetTypeSelection = remember {
            mutableStateOf(false)
        }
        val assetType = remember {
            mutableStateOf(AssetType.OTHER)
        }
        val assetName = remember {
            mutableStateOf("")
        }
        val estimatedValue = remember {
            mutableStateOf("")
        }
        val annAnnualizedRateOfReturn = remember {
            mutableStateOf("")
        }
        val showDialog = remember {
            mutableStateOf<DateSelectionType?>(null)
        }
        val effectiveDate = remember {
            mutableStateOf("")
        }
        val revisitDate = remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 30.dp, vertical = 24.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterStart)
                        .clickable {
                            onBackButtonFromExternalScreenClicked()
                        },
                    painter = painterResource(id = com.fintexinc.core.R.drawable.ic_back_arrow),
                    contentDescription = stringResource(R.string.description_icon_back),
                    tint = Colors.BackgroundPrimary
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 18.dp)
                        .align(Alignment.Center),
                    text = stringResource(R.string.title_add_an_asset),
                    style = FontStyles.HeadingRegular,
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .shadow(2.dp)
            )
            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            color = Colors.BackgroundSupplementary,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .drawBehind {
                            drawRect(
                                color = Colors.BorderInformation,
                                topLeft = Offset(0f, 0f),
                                size = Size(4.dp.toPx(), size.height)
                            )
                        }
                        .padding(start = 12.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.text_asset_will_update_chart),
                        style = FontStyles.BodyMediumBold,
                        color = Colors.Text
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Text(
                        text = stringResource(R.string.text_you_can_remove_asset_later),
                        style = FontStyles.BodyMedium,
                        color = Colors.BrandBlack,
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                AddItemSelection(
                    title = stringResource(R.string.text_asset_type),
                    text = assetType.value.label.ifEmpty { stringResource(R.string.text_make_selection) },
                    onAddItemSelectionClicked = {
                        showAssetTypeSelection.value = true
                    }
                )
                AddItemText(
                    title = stringResource(R.string.text_asset_name),
                    hint = stringResource(R.string.text_enter_name),
                    onTextChanged = { text ->
                        assetName.value = text
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemText(
                    title = stringResource(R.string.text_estimated_value),
                    hint = stringResource(R.string.text_currency),
                    onTextChanged = { text ->
                        estimatedValue.value = text
                    },
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemText(
                    title = stringResource(R.string.text_annualized_rate_of_return),
                    hint = stringResource(R.string.text_percent),
                    onTextChanged = { text ->
                        annAnnualizedRateOfReturn.value = text
                    },
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemSelection(
                    title = stringResource(R.string.text_effective_date),
                    text = effectiveDate.value,
                    onAddItemSelectionClicked = {
                        showDialog.value = DateSelectionType.EffectiveDate
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemSelection(
                    title = stringResource(R.string.text_revisit_date),
                    text = revisitDate.value,
                    onAddItemSelectionClicked = {
                        showDialog.value = DateSelectionType.RevisitDate
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 18.dp)
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                color = Colors.BackgroundPrimary,
                                shape = RoundedCornerShape(40.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = Colors.Primary,
                                shape = RoundedCornerShape(40.dp)
                            )
                            .clickableShape(RoundedCornerShape(40.dp)) {
                                onSaveAssetClick(
                                    Custom(
                                        id = "",
                                        userId = "",
                                        assetName = assetName.value,
                                        assetType = assetType.value,
                                        assetValue = estimatedValue.value.toDoubleOrNull() ?: 0.0,
                                        linkedDate = effectiveDate.value,
                                        lastUpdated = revisitDate.value
                                    )
                                )
                            }
                            .padding(12.dp)
                            .align(Alignment.Center),
                        text = stringResource(R.string.text_add_asset),
                        textAlign = TextAlign.Center,
                        style = FontStyles.HeadingLarge,
                        color = Colors.TextInverse
                    )
                }
            }
        }
        if (showAssetTypeSelection.value) {
            ItemTypeSelection (
                itemTypes = AssetType.entries,
                itemTypeTitle = stringResource(R.string.text_asset_type),
                onItemTypeSelected = {
                    assetType.value = it as AssetType
                    showAssetTypeSelection.value = false
                },
                onCancel = {
                    showAssetTypeSelection.value = false
                }
            )
        }
        if (showDialog.value != null) {
            @OptIn(ExperimentalMaterial3Api::class) val datePickerState = rememberDatePickerState()
            @OptIn(ExperimentalMaterial3Api::class) DatePickerDialog(onDismissRequest = {
                showDialog.value = null
            }, confirmButton = {
                Button(onClick = {
                    val dateFormatter = DateUtils.formatMillisToDateSimpleDateFormat(
                        datePickerState.selectedDateMillis ?: 0L
                    )
                    when (showDialog.value) {
                        is DateSelectionType.EffectiveDate -> {
                            effectiveDate.value = dateFormatter
                        }

                        is DateSelectionType.RevisitDate -> {
                            revisitDate.value = dateFormatter
                        }

                        else -> {}
                    }
                    showDialog.value = null
                }) {
                    Text(
                        stringResource(R.string.text_ok), style = FontStyles.BodyMedium
                    )
                }
            }, dismissButton = {
                Button(onClick = { showDialog.value = null }) {
                    Text(
                        stringResource(R.string.text_cancel), style = FontStyles.BodyMedium
                    )
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }
    }
}