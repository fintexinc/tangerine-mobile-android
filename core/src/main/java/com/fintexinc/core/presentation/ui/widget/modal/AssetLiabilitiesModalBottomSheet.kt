package com.fintexinc.core.presentation.ui.widget.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.presentation.ui.widget.TangerineSearchBar
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
        shape = RoundedCornerShape(16.dp),
        containerColor = Colors.Background,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = title,
                    style = FontStyles.TitleMediumMedium,
                    color = Colors.BrandBlack
                )
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            isShowing.value = false
                            updateCheckedStates(
                                assets,
                                assetsCheckStates,
                                liabilities,
                                liabilitiesCheckStates
                            )
                        },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Colors.BorderSubdued)
                )
            }
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        if (liabilitiesMainCheckState.value) {
                            for (i in liabilitiesCheckStates.indices) {
                                liabilitiesCheckStates[i] = false
                            }
                            liabilitiesMainCheckState.value = false
                        } else {
                            for (i in liabilitiesCheckStates.indices) {
                                liabilitiesCheckStates[i] = true
                            }
                            liabilitiesMainCheckState.value = true
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                        text = liability.name,
                        style = FontStyles.BodyLarge,
                        color = Colors.BrandBlack
                    )
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        text = liability.value.formatCurrency(),
                        style = FontStyles.BodyLarge,
                        color = Colors.TextSubdued,
                        textAlign = TextAlign.End
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Colors.BorderSubdued)
                )
            }
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