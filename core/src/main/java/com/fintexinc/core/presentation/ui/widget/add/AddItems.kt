package com.fintexinc.core.presentation.ui.widget.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.data.model.ItemType
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun AddItemSelection(
    title: String,
    text: String,
    info: String = "",
    onAddItemSelectionClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Colors.Background,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable {
                    onAddItemSelectionClicked()
                }
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = title,
                    style = FontStyles.BodyMedium,
                    color = Colors.Text
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = text,
                    style = FontStyles.BodyLarge,
                    color = Colors.TextSubdued
                )
                if(info.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(color = Colors.BackgroundSubdued)
                            .padding(8.dp),
                        text = info,
                        style = FontStyles.BodySmall,
                        color = Colors.Text
                    )
                }
            }
            Icon(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 6.dp),
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = stringResource(R.string.description_icon_add),
                tint = Colors.BrandBlack
            )
        }

        HorizontalDivider()
    }
}

@Composable
fun AddItemText(
    title: String,
    hint: String,
    info: String = "",
    text: String = "",
    onTextChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val text = remember {
        mutableStateOf(text)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = title,
            style = FontStyles.BodyMedium,
            color = Colors.Text
        )
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = text.value,
            onValueChange = { newText ->
                text.value = newText
                onTextChanged(newText)
            },
            textStyle = FontStyles.BodyLarge,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            decorationBox = { innerTextField ->
                when {
                    info.isNotEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            innerTextField()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(color = Colors.BackgroundSubdued)
                                    .padding(8.dp),
                                text = info,
                                style = FontStyles.BodySmall,
                                color = Colors.Text
                            )
                        }
                    }

                    text.value.isEmpty() -> {
                        Box {
                            Text(
                                text = hint,
                                style = FontStyles.BodyLarge,
                                color = Colors.TextSubdued
                            )
                            innerTextField()
                        }
                    }

                    else -> {
                        innerTextField()
                    }
                }
            },
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        HorizontalDivider()
    }
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .background(color = Colors.BorderSubdued)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTypeSelection(
    itemTypeTitle: String,
    itemTypes: List<ItemType>,
    onItemTypeSelected: (ItemType) -> Unit,
    onCancel: () -> Unit
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            onCancel()
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Colors.Background,
        dragHandle = null
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(0.33f)
                        .wrapContentHeight()
                        .clickable {
                            onCancel()
                        },
                    text = "Cancel",
                    style = FontStyles.BodyLarge,
                    color = Colors.TextInteractive
                )
                Text(
                    modifier = Modifier
                        .weight(0.33f)
                        .wrapContentHeight(),
                    text = itemTypeTitle,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.BrandBlack
                )
                Spacer(modifier = Modifier.weight(0.33f))
            }
            HorizontalDivider(color = Colors.BorderSubdued)
            itemTypes.forEach {
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable {
                                onItemTypeSelected(it)
                            }
                            .padding(horizontal = 30.dp, vertical = 18.dp),
                        text = it.label,
                        style = FontStyles.BodyLarge,
                        color = Colors.BrandBlack,
                    )
                    HorizontalDivider(
                        color = Colors.BorderSubdued,
                        modifier = Modifier.padding(horizontal = 30.dp),
                    )
                }
            }
        }
    }
}

sealed class DateSelectionType {
    object EffectiveDate : DateSelectionType()
    object RevisitDate : DateSelectionType()
}