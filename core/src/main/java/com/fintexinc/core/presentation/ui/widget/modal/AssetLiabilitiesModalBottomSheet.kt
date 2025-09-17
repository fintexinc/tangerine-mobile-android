package com.fintexinc.core.presentation.ui.widget.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
<<<<<<< HEAD
=======
import androidx.compose.ui.res.painterResource
>>>>>>> a9b6912014d50aa6858993015134f528286faaf3
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
<<<<<<< HEAD
import com.fintexinc.core.presentation.ui.modifier.clickableShape
=======
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.presentation.ui.widget.TangerineSearchBar
>>>>>>> a9b6912014d50aa6858993015134f528286faaf3
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetLiabilitiesModalBottomSheet(
    isShowing: MutableState<Boolean>,
    title: String,
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>,
    updateCheckedStates: (List<NameValueChecked>, List<Boolean>, List<NameValueChecked>, List<Boolean>) -> Unit
) {
    val assetMainCheckState = remember {
        mutableStateOf(true)
    }
    val assetsCheckStates = remember {
        mutableStateListOf(*assets.map { it.isChecked }.toTypedArray())
    }
    val liabilitiesMainCheckState = remember {
        mutableStateOf(true)
    }
    val liabilitiesCheckStates = remember {
        mutableStateListOf(*liabilities.map { it.isChecked }.toTypedArray())
    }
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            isShowing.value = false
            updateCheckedStates(assets, assetsCheckStates, liabilities, liabilitiesCheckStates)
        },
        shape = RoundedCornerShape(8.dp),
        containerColor = Colors.Background,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(0.33f)
                        .wrapContentHeight()
                        .clickable {
                            isShowing.value = false
                        },
<<<<<<< HEAD
                    text = stringResource(R.string.text_cancel),
                    style = FontStyles.BodyLarge,
                    color = Colors.TextInteractive
                )
                Text(
                    modifier = Modifier
                        .weight(0.33f)
                        .wrapContentHeight(),
                    text = title,
                    textAlign = TextAlign.Center,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.BrandBlack
                )
                Text(
=======
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.description_icon_close),
                )

            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = "10 of 10 Accounts Selected",
                style = FontStyles.BodyLarge,
                color = Colors.TextSubdued
            )
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            TangerineSearchBar(contentPadding = PaddingValues(0.dp))
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        if (assetMainCheckState.value) {
                            for (i in assetsCheckStates.indices) {
                                assetsCheckStates[i] = false
                            }
                            assetMainCheckState.value = false
                        } else {
                            for (i in assetsCheckStates.indices) {
                                assetsCheckStates[i] = true
                            }
                            assetMainCheckState.value = true
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (assetMainCheckState.value) {
                    Image(
                        modifier = Modifier
                            .wrapContentSize(),
                        painter = painterResource(R.drawable.ic_checkmark),
                        contentDescription = stringResource(R.string.description_icon_check),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .width(26.dp)
                            .height(34.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .size(26.dp)
                                .background(color = Colors.Background, CircleShape)
                                .border(
                                    1.dp, Colors.BackgroundPrimary, CircleShape
                                )
                                .align(Alignment.Center)
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.width(16.dp)
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(end = 24.dp),
                    text = stringResource(R.string.text_assets_count, assets.size),
                    style = FontStyles.BodyLarge,
                    color = Colors.BrandBlack
                )
            }
            assets.forEachIndexed { index, asset ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            assetsCheckStates[index] = !assetsCheckStates[index]
                        }
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (assetsCheckStates[index]) {
                        Image(
                            modifier = Modifier
                                .wrapContentSize(),
                            painter = painterResource(R.drawable.ic_checkmark),
                            contentDescription = stringResource(R.string.description_icon_checkmark),
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .width(26.dp)
                                .height(34.dp)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(color = Colors.Background, CircleShape)
                                    .border(
                                        1.dp, Colors.BackgroundPrimary, CircleShape
                                    )
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier.width(16.dp)
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                            .padding(end = 24.dp),
                        text = asset.name,
                        style = FontStyles.BodyLarge,
                        color = Colors.BrandBlack
                    )
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = asset.value.formatCurrency(),
                        style = FontStyles.BodyLarge,
                        color = Colors.TextSubdued,
                        textAlign = TextAlign.End
                    )
                }
                Spacer(
>>>>>>> a9b6912014d50aa6858993015134f528286faaf3
                    modifier = Modifier
                        .weight(0.33f)
                        .wrapContentHeight(),
                    text = stringResource(R.string.text_done),
                    textAlign = TextAlign.End,
                    style = FontStyles.BodyLargeBold,
                    color = Colors.BrandBlack
                )
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .background(Colors.BorderSubdued)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
            ) {
<<<<<<< HEAD
                ChipsWithTitle(
                    title = stringResource(R.string.text_assets),
                    items = assets,
                    checkStates = assetsCheckStates,
                    onItemCheckedChange = { index, isChecked ->
                        assetsCheckStates[index] = isChecked
=======
                if (liabilitiesMainCheckState.value) {
                    Image(
                        modifier = Modifier
                            .wrapContentSize(),
                        painter = painterResource(R.drawable.ic_checkmark),
                        contentDescription = stringResource(R.string.description_icon_check),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .width(26.dp)
                            .height(34.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .size(26.dp)
                                .background(color = Colors.Background, CircleShape)
                                .border(
                                    1.dp, Colors.BackgroundPrimary, CircleShape
                                )
                                .align(Alignment.Center)
                        )
>>>>>>> a9b6912014d50aa6858993015134f528286faaf3
                    }
                )
<<<<<<< HEAD
                Spacer(modifier = Modifier.height(12.dp))
                ChipsWithTitle(
                    title = stringResource(R.string.text_liabilities),
                    items = liabilities,
                    checkStates = liabilitiesCheckStates,
                    onItemCheckedChange = { index, isChecked ->
                        liabilitiesCheckStates[index] = isChecked
                    }
                )
            }
        }

    }
}

@Composable
private fun ChipsWithTitle(
    title: String,
    items: List<NameValueChecked>,
    checkStates: MutableList<Boolean>,
    onItemCheckedChange: (Int, Boolean) -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        text = title,
        style = FontStyles.BodyLargeBold,
        color = Colors.BrandBlack
    )
    Spacer(modifier = Modifier.height(8.dp))
    // Chips
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEachIndexed { index, item ->
            val checked = checkStates[index]
            Text(
                modifier = Modifier
                    .background(
                        color = Colors.BackgroundInteractive,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .then(
                        if (checked) {
                            Modifier.border(
                                width = 1.dp,
                                color = Colors.TextInteractive,
                                shape = RoundedCornerShape(16.dp)
=======
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(end = 24.dp),
                    text = stringResource(R.string.text_liabilities_count, liabilities.size),
                    style = FontStyles.BodyLarge,
                    color = Colors.BrandBlack
                )
            }
            liabilities.forEachIndexed { index, liability ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            liabilitiesCheckStates[index] = !liabilitiesCheckStates[index]
                        }
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (liabilitiesCheckStates[index]) {
                        Image(
                            modifier = Modifier
                                .wrapContentSize(),
                            painter = painterResource(R.drawable.ic_checkmark),
                            contentDescription = stringResource(R.string.description_icon_close),
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .width(26.dp)
                                .height(34.dp)
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(color = Colors.Background, CircleShape)
                                    .border(
                                        1.dp, Colors.BackgroundPrimary, CircleShape
                                    )
                                    .align(Alignment.Center)
>>>>>>> a9b6912014d50aa6858993015134f528286faaf3
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickableShape(RoundedCornerShape(16.dp)) {
                        val newChecked = !checkStates[index]
                        checkStates[index] = newChecked
                        onItemCheckedChange(index, newChecked)
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = item.name,
                style = FontStyles.BodyMedium,
                color = Colors.TextInteractive
            )
        }
    }
}

data class NameValueChecked(
    val name: String,
    val subName: String,
    val date: String,
    val value: Double,
    val isChecked: Boolean = true
)