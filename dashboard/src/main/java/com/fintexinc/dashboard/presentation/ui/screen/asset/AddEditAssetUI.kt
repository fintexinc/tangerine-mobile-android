package com.fintexinc.dashboard.presentation.ui.screen.asset

import androidx.compose.foundation.background
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
import com.fintexinc.core.presentation.ui.widget.add.AddItemSelection
import com.fintexinc.core.presentation.ui.widget.add.AddItemText
import com.fintexinc.core.presentation.ui.widget.add.DateSelectionType
import com.fintexinc.core.presentation.ui.widget.add.ItemTypeSelection
import com.fintexinc.core.presentation.ui.widget.button.PrimaryButton
import com.fintexinc.core.presentation.ui.widget.button.SecondaryButton
import com.fintexinc.core.presentation.ui.widget.dialog.DeletePopup
import com.fintexinc.core.presentation.ui.widget.dialog.UpdatePopup
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R
import com.fintexinc.dashboard.presentation.ui.screen.asset.error.AssetError
import com.fintexinc.dashboard.presentation.ui.screen.asset.error.FieldValidation
import java.util.UUID

@Composable
fun AddEditAssetUI(
    asset: Custom?,
    onSaveAssetClick: (asset: Custom) -> Unit,
    onDeleteAsset: (asset: Custom) -> Unit,
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
        val showUpdatePopup = remember {
            mutableStateOf(false)
        }
        val showDeletePopup = remember {
            mutableStateOf(false)
        }
        val assetType = remember {
            mutableStateOf(asset?.assetType)
        }
        val assetName = remember {
            mutableStateOf(asset?.assetName ?: "")
        }
        val estimatedValue = remember {
            mutableStateOf(if (asset?.assetValue == null) "" else asset.assetValue.toString())
        }
        val annAnnualizedRateOfReturn = remember {
            mutableStateOf("")
        }
        val showDialog = remember {
            mutableStateOf<DateSelectionType?>(null)
        }
        val effectiveDate = remember {
            mutableStateOf(asset?.lastUpdated ?: "")
        }
        val revisitDate = remember {
            mutableStateOf(asset?.linkedDate ?: "")
        }
        val assetValidation = remember {
            mutableStateOf(
                hashMapOf(
                    "assetType" to FieldValidation(true),
                    "assetName" to FieldValidation(true),
                    "estimatedValue" to FieldValidation(true),
                    "effectiveDate" to FieldValidation(true),
                    "revisitDate" to FieldValidation(true)
                ).toMap()
            )
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
                    text = if (asset != null) {
                        stringResource(R.string.text_edit_asset)
                    } else {
                        stringResource(R.string.title_add_an_asset)
                    },
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
                        text = stringResource(R.string.text_gain_complete_picture_asset),
                        style = FontStyles.BodyMediumBold,
                        color = Colors.Text
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Text(
                        text = stringResource(R.string.text_you_can_remove_later, stringResource(R.string.text_asset_lowercase)),
                        style = FontStyles.BodyMedium,
                        color = Colors.BrandBlack,
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                AddItemSelection(
                    title = stringResource(R.string.text_asset_type),
                    text = assetType.value?.label ?: stringResource(R.string.text_make_selection),
                    errorRes = getErrorResIdOrNull("assetType", assetValidation.value),
                    onAddItemSelectionClicked = {
                        showAssetTypeSelection.value = true
                    }
                )
                AddItemText(
                    title = stringResource(R.string.text_asset_name),
                    text = assetName.value,
                    hint = stringResource(R.string.text_enter_name),
                    errorRes = getErrorResIdOrNull("assetName", assetValidation.value),
                    onTextChanged = { text ->
                        assetName.value = text
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemText(
                    title = stringResource(R.string.text_estimated_value),
                    text = estimatedValue.value,
                    hint = stringResource(R.string.text_currency),
                    errorRes = getErrorResIdOrNull("estimatedValue", assetValidation.value),
                    onTextChanged = { text ->
                        estimatedValue.value = text
                    },
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemText(
                    title = stringResource(R.string.text_annualized_rate_of_return),
                    hint = stringResource(R.string.text_percent),
                    info = stringResource(R.string.text_ann_rate_of_return_info),
                    onTextChanged = { text ->
                        annAnnualizedRateOfReturn.value = text
                    },
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemSelection(
                    title = stringResource(R.string.text_effective_date),
                    text = effectiveDate.value,
                    errorRes = getErrorResIdOrNull("effectiveDate", assetValidation.value),
                    info = stringResource(R.string.text_revisited_date_info, "asset"),
                    onAddItemSelectionClicked = {
                        showDialog.value = DateSelectionType.EffectiveDate
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemSelection(
                    title = stringResource(R.string.text_revisit_date),
                    text = revisitDate.value,
                    errorRes = getErrorResIdOrNull("revisitDate", assetValidation.value),
                    onAddItemSelectionClicked = {
                        showDialog.value = DateSelectionType.RevisitDate
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                if (asset != null) {
                    Column {
                        PrimaryButton(stringResource(R.string.text_edit_asset)) {
                            showUpdatePopup.value = true
                        }
                        SecondaryButton(
                            text = stringResource(R.string.format_delete_item, "Asset"),
                            onClick = {
                                showDeletePopup.value = true
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                } else {
                    PrimaryButton(stringResource(R.string.text_add, "Asset")) {
                        val validationResult = validateAsset(
                            assetType.value, assetName.value, estimatedValue.value,
                            effectiveDate.value, revisitDate.value
                        )
                        if (validationResult.any { pair -> !pair.value.isValid }) {
                            assetValidation.value = validationResult
                            return@PrimaryButton
                        }
                        onSaveAssetClick(
                            Custom(
                                id = asset?.id ?: UUID.randomUUID().toString(),
                                userId = asset?.userId ?: "",
                                assetName = assetName.value,
                                assetType = assetType.value ?: AssetType.OTHER,
                                assetValue = estimatedValue.value.toDoubleOrNull()
                                    ?: 0.0,
                                linkedDate = revisitDate.value,
                                lastUpdated = effectiveDate.value
                            )
                        )
                    }
                }
            }
        }
        if (showAssetTypeSelection.value) {
            ItemTypeSelection(
                itemTypes = AssetType.entries,
                itemTypeTitle = stringResource(R.string.text_asset_type),
                onItemTypeSelected = {
                    assetType.value = it as AssetType
                    assetValidation.value = assetValidation.value.toMutableMap().also { map ->
                        map["assetType"] = FieldValidation(true)
                    }.toMap()
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
                            assetValidation.value =
                                assetValidation.value.toMutableMap().also { map ->
                                    map["effectiveDate"] = FieldValidation(true)
                                }.toMap()
                            effectiveDate.value = dateFormatter
                        }

                        is DateSelectionType.RevisitDate -> {
                            assetValidation.value =
                                assetValidation.value.toMutableMap().also { map ->
                                    map["effectiveDate"] = FieldValidation(true)
                                }.toMap()
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
        if (showUpdatePopup.value) {
            UpdatePopup(
                imageRes = R.drawable.ic_update_successful,
                imageContentDescription = stringResource(R.string.description_icon_update_successful),
                title = stringResource(R.string.text_update_successful),
                text = stringResource(R.string.text_change_may_affect)
            ) {
                showUpdatePopup.value = false
                val validationResult = validateAsset(
                    assetType.value, assetName.value, estimatedValue.value,
                    effectiveDate.value, revisitDate.value
                )
                if (validationResult.any { pair -> !pair.value.isValid }) {
                    assetValidation.value = validationResult
                    return@UpdatePopup
                }
                onSaveAssetClick(
                    Custom(
                        id = asset?.id ?: UUID.randomUUID().toString(),
                        userId = asset?.userId ?: "",
                        assetName = assetName.value,
                        assetType = assetType.value ?: AssetType.OTHER,
                        assetValue = estimatedValue.value.toDoubleOrNull()
                            ?: 0.0,
                        linkedDate = revisitDate.value,
                        lastUpdated = effectiveDate.value
                    )
                )
            }
        }
        if (showDeletePopup.value) {
            DeletePopup(
                imageRes = R.drawable.ic_delete_item,
                imageContentDescription = stringResource(R.string.description_icon_delete_item),
                title = stringResource(
                    R.string.format_delete_item,
                    asset?.assetName ?: assetName.value
                ),
                text = stringResource(R.string.text_are_you_sure_you_want_to_delete_asset),
                onDeleteClick = {
                    asset?.let {
                        onDeleteAsset(asset)
                    }
                },
                onCancelClick = {
                    showDeletePopup.value = false
                }
            )
        }
    }
}

private fun validateAsset(
    assetType: AssetType?,
    assetName: String,
    estimatedValue: String,
    effectiveDate: String,
    revisitDate: String
): Map<String, FieldValidation> {
    val assetValidationResult = hashMapOf<String, FieldValidation>()
    assetValidationResult["assetType"] = when {
        assetType == null -> FieldValidation(
            isValid = false,
            assetError = AssetError.ASSET_TYPE_NOT_SELECTED
        )

        else -> FieldValidation(isValid = true)
    }
    assetValidationResult["assetName"] = when {
        assetName.isEmpty() -> FieldValidation(
            isValid = false,
            assetError = AssetError.ASSET_NAME_MISSING
        )

        else -> FieldValidation(isValid = true)
    }
    val estimatedValueDouble = estimatedValue.toDoubleOrNull() ?: 0.0
    assetValidationResult["estimatedValue"] = when {
        estimatedValue.isEmpty() -> FieldValidation(
            isValid = false,
            assetError = AssetError.BALANCE_IS_MISSING
        )

        estimatedValueDouble < 0 -> FieldValidation(
            isValid = false,
            assetError = AssetError.BALANCE_NEGATIVE
        )

        else -> FieldValidation(isValid = true)
    }
    assetValidationResult["effectiveDate"] = when {
        effectiveDate.isEmpty() -> FieldValidation(
            isValid = false,
            assetError = AssetError.EFFECTIVE_DATE_MISSING
        )

        DateUtils.isDateInFuture(effectiveDate) -> FieldValidation(
            isValid = false,
            assetError = AssetError.EFFECTIVE_DATE_IN_FUTURE
        )

        else -> FieldValidation(isValid = true)
    }
    assetValidationResult["revisitDate"] = when {
        revisitDate.isNotEmpty() && DateUtils.isDateInPast(revisitDate) -> FieldValidation(
            isValid = false,
            assetError = AssetError.REVISIT_DATE_IN_PAST
        )

        else -> FieldValidation(isValid = true)
    }

    return assetValidationResult.toMap()
}

private fun getErrorResIdOrNull(
    fieldName: String,
    assetValidation: Map<String, FieldValidation>
): Int? {
    return assetValidation[fieldName]?.assetError?.messageResId
}