package com.fintexinc.dashboard.presentation.ui.screen

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
import com.fintexinc.core.domain.model.Liability
import com.fintexinc.core.domain.model.LiabilityType
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
fun AddEditLiabilityUI(
    liability: Liability?,
    onSaveLiabilityClick: (liability: Liability) -> Unit,
    onDeleteLiability: (liability: Liability) -> Unit,
    onBackButtonFromExternalScreenClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.Background)
    ) {
        val showLiabilityTypeSelection = remember {
            mutableStateOf(false)
        }
        val liabilityType = remember {
            mutableStateOf(liability?.liabilityType)
        }
        val liabilityName = remember {
            mutableStateOf(liability?.liabilityType?.label ?: "")
        }
        val currentBalance = remember {
            mutableStateOf(liability?.balance?.toString() ?: "")
        }
        val monthlyPayment = remember {
            mutableStateOf(liability?.limit?.toString() ?: "")
        }
        val annualInterestRate = remember {
            mutableStateOf(liability?.interestRate?.toString() ?: "")
        }
        val effectiveDate = remember {
            mutableStateOf(liability?.lastUpdated ?: "")
        }
        val revisitDate = remember {
            mutableStateOf(liability?.linkedDate ?: "")
        }
        val showDialog = remember {
            mutableStateOf<DateSelectionType?>(null)
        }
        val showUpdatePopup = remember {
            mutableStateOf(false)
        }
        val showDeletePopup = remember {
            mutableStateOf(false)
        }
        val liabilityValidation = remember {
            mutableStateOf(
                hashMapOf(
                    "liabilityType" to FieldValidation(true),
                    "liabilityName" to FieldValidation(true),
                    "balance" to FieldValidation(true),
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
                    text = if (liability != null) {
                        stringResource(R.string.title_edit_liability)
                    } else {
                        stringResource(R.string.title_add_liability)
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
                        text = stringResource(R.string.text_gain_complete_picture_liability),
                        style = FontStyles.BodyMediumBold,
                        color = Colors.Text
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Text(
                        text = stringResource(R.string.text_you_can_remove_later, stringResource(R.string.text_liability_lowercase)),
                        style = FontStyles.BodyMedium,
                        color = Colors.BrandBlack,
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))

                AddItemSelection(
                    title = stringResource(R.string.text_liability_type),
                    text = liabilityType.value?.label?.takeIf { it.isNotEmpty() }
                        ?: stringResource(R.string.text_make_selection),
                    errorRes = getErrorResIdOrNull(
                        "liabilityType",
                        liabilityValidation.value
                    ),
                    onAddItemSelectionClicked = {
                        showLiabilityTypeSelection.value = true
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemText(
                    title = stringResource(R.string.text_liability_name),
                    hint = stringResource(R.string.text_enter_name),
                    text = liabilityName.value,
                    errorRes = getErrorResIdOrNull(
                        "liabilityName",
                        liabilityValidation.value
                    ),
                    onTextChanged = { text ->
                        liabilityName.value = text
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemText(
                    title = stringResource(R.string.text_current_balance),
                    hint = stringResource(R.string.text_currency),
                    text = currentBalance.value,
                    errorRes = getErrorResIdOrNull(
                        "balance",
                        liabilityValidation.value
                    ),
                    onTextChanged = { text ->
                        currentBalance.value = text
                    },
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemText(
                    title = stringResource(R.string.text_monthly_payment),
                    hint = stringResource(R.string.text_currency),
                    text = monthlyPayment.value,
                    onTextChanged = { text ->
                        monthlyPayment.value = text
                    },
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemText(
                    title = stringResource(R.string.text_annual_interest_rate),
                    hint = stringResource(R.string.text_percent),
                    info = stringResource(R.string.text_annual_interest_rate_info),
                    text = annualInterestRate.value,
                    onTextChanged = { text ->
                        annualInterestRate.value = text
                    },
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemSelection(
                    title = stringResource(R.string.text_effective_date),
                    text = effectiveDate.value,
                    info = stringResource(R.string.text_revisited_date_info, "liability"),
                    errorRes = getErrorResIdOrNull(
                        "effectiveDate",
                        liabilityValidation.value
                    ),
                    onAddItemSelectionClicked = {
                        showDialog.value = DateSelectionType.EffectiveDate
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
                AddItemSelection(
                    title = stringResource(R.string.text_revisit_date),
                    text = revisitDate.value,
                    errorRes = getErrorResIdOrNull(
                        "revisitDate",
                        liabilityValidation.value
                    ),
                    onAddItemSelectionClicked = {
                        showDialog.value = DateSelectionType.RevisitDate
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                if (liability != null) {
                    Column {
                        PrimaryButton(stringResource(R.string.text_confirm_changes)) {
                            showUpdatePopup.value = true
                        }
                        SecondaryButton(
                            text = stringResource(R.string.format_delete_item, "Liability"),
                            onClick = {
                                showDeletePopup.value = true
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                } else {
                    PrimaryButton(stringResource(R.string.text_add, "Liability")) {
                        val validationResult = validateLiability(
                            liabilityType = liabilityType.value,
                            liabilityName = liabilityName.value,
                            balance = currentBalance.value,
                            effectiveDate = effectiveDate.value,
                            revisitDate = revisitDate.value
                        )
                        if(validationResult.values.any { !it.isValid }) {
                            liabilityValidation.value = validationResult
                            return@PrimaryButton
                        }
                        onSaveLiabilityClick(
                            Liability(
                                id = liability?.id ?: UUID.randomUUID().toString(),
                                userId = liability?.userId ?: "",
                                liabilityType = liabilityType.value ?: LiabilityType.OTHER,
                                accountNumber = UUID.randomUUID().toString(),
                                balance = currentBalance.value.toDoubleOrNull() ?: 0.0,
                                linkedDate = effectiveDate.value,
                                limit = monthlyPayment.value.toDoubleOrNull() ?: 0.0,
                                interestRate = annualInterestRate.value.toDoubleOrNull() ?: 0.0,
                                currency = "$",
                                lastUpdated = effectiveDate.value,
                                isCustomLiability = true
                            )
                        )
                    }
                }
            }
        }
        if (showLiabilityTypeSelection.value) {
            ItemTypeSelection(
                itemTypes = LiabilityType.entries,
                itemTypeTitle = stringResource(R.string.text_asset_type),
                onItemTypeSelected = {
                    liabilityType.value = it as LiabilityType
                    showLiabilityTypeSelection.value = false
                },
                onCancel = {
                    showLiabilityTypeSelection.value = false
                }
            )
        }
        if (showDialog.value != null) {
            @OptIn(ExperimentalMaterial3Api::class) val datePickerState = rememberDatePickerState()
            @OptIn(ExperimentalMaterial3Api::class) DatePickerDialog(
                onDismissRequest = {
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
                },
                dismissButton = {
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
                val validationResult = validateLiability(
                    liabilityType = liabilityType.value,
                    liabilityName = liabilityName.value,
                    balance = currentBalance.value,
                    effectiveDate = effectiveDate.value,
                    revisitDate = revisitDate.value
                )
                if(validationResult.values.any { !it.isValid }) {
                    liabilityValidation.value = validationResult
                    return@UpdatePopup
                }
                onSaveLiabilityClick(
                    Liability(
                        id = liability?.id ?: UUID.randomUUID().toString(),
                        userId = liability?.userId ?: "",
                        liabilityType = liabilityType.value ?: LiabilityType.OTHER,
                        accountNumber = UUID.randomUUID().toString(),
                        balance = currentBalance.value.toDoubleOrNull() ?: 0.0,
                        linkedDate = effectiveDate.value,
                        limit = monthlyPayment.value.toDoubleOrNull() ?: 0.0,
                        interestRate = annualInterestRate.value.toDoubleOrNull() ?: 0.0,
                        currency = "$",
                        lastUpdated = effectiveDate.value,
                        isCustomLiability = true
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
                    liability?.liabilityType?.label ?: liabilityName.value
                ),
                text = stringResource(R.string.text_are_you_sure_you_want_to_delete_asset),
                onDeleteClick = {
                    liability?.let {
                        onDeleteLiability(liability)
                    }
                },
                onCancelClick = {
                    showDeletePopup.value = false
                }
            )
        }
    }
}

private fun validateLiability(
    liabilityType: LiabilityType?,
    liabilityName: String,
    balance: String,
    effectiveDate: String,
    revisitDate: String
): Map<String, FieldValidation> {
    val assetValidationResult = hashMapOf<String, FieldValidation>()
    assetValidationResult["liabilityType"] = when {
        liabilityType == null -> FieldValidation(
            isValid = false,
            assetError = AssetError.ASSET_TYPE_NOT_SELECTED
        )

        else -> FieldValidation(isValid = true)
    }
    assetValidationResult["liabilityName"] = when {
        liabilityName.isEmpty() -> FieldValidation(
            isValid = false,
            assetError = AssetError.ASSET_NAME_MISSING
        )

        else -> FieldValidation(isValid = true)
    }
    val estimatedValueDouble = balance.toDoubleOrNull() ?: 0.0
    assetValidationResult["balance"] = when {
        balance.isEmpty() -> FieldValidation(
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