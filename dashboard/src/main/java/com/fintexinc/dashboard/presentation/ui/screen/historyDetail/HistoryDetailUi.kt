package com.fintexinc.dashboard.presentation.ui.screen.historyDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.widget.ToolBar
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.TextButton
import com.fintexinc.core.ui.components.UniversalDataSection
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.core.ui.models.DataSectionItemUi
import com.fintexinc.dashboard.R

@Composable
fun HistoryDetailUi(
    onBackClicked: () -> Unit,
    onEditAssetNavigate: (String) -> Unit,
) {
    // TODO() mock data
    val mockData = listOf(
        DataSectionItemUi(
            label = stringResource(R.string.text_asset_type),
            value = "Luxury Assets",
            valueColor = Colors.BrandBlack,
            hasInfoIcon = false,
            hasTrailingIcon = false,
            valueStyle = FontStyles.BodyLarge,
            labelStyle = FontStyles.BodyMedium,
            isHighlighted = false,
            showColorDot = false,
            dotColor = Color.Transparent,
            isMultiline = false
        ),
        DataSectionItemUi(
            label = stringResource(R.string.text_asset_name),
            value = "Boat",
            valueColor = Colors.BrandBlack,
            hasInfoIcon = false,
            hasTrailingIcon = false,
            valueStyle = FontStyles.BodyLarge,
            labelStyle = FontStyles.BodyMedium,
            isHighlighted = false,
            showColorDot = false,
            dotColor = Color.Transparent,
            isMultiline = false
        ),
        DataSectionItemUi(
            label = stringResource(R.string.text_estimated_value),
            value = "$500,000",
            valueColor = Colors.BrandBlack,
            hasInfoIcon = false,
            hasTrailingIcon = false,
            valueStyle = FontStyles.BodyLarge,
            labelStyle = FontStyles.BodyMedium,
            isHighlighted = false,
            showColorDot = false,
            dotColor = Color.Transparent,
            isMultiline = false
        ),
        DataSectionItemUi(
            label = stringResource(R.string.text_annualized_rate_of_return_optional),
            value = "3%",
            valueColor = Colors.BrandBlack,
            hasInfoIcon = false,
            hasTrailingIcon = false,
            valueStyle = FontStyles.BodyLarge,
            labelStyle = FontStyles.BodyMedium,
            isHighlighted = false,
            showColorDot = false,
            dotColor = Color.Transparent,
            isMultiline = true
        ),
        DataSectionItemUi(
            label = stringResource(R.string.text_effective_date),
            value = "MAY 2, 2024",
            valueColor = Colors.BrandBlack,
            hasInfoIcon = false,
            hasTrailingIcon = false,
            valueStyle = FontStyles.BodyLarge,
            labelStyle = FontStyles.BodyMedium,
            isHighlighted = false,
            showColorDot = false,
            dotColor = Color.Transparent,
            isMultiline = false
        ),
        DataSectionItemUi(
            label = stringResource(R.string.text_revisit_date_optional),
            value = "MAR 2, 2025",
            valueColor = Colors.BrandBlack,
            hasInfoIcon = false,
            hasTrailingIcon = false,
            valueStyle = FontStyles.BodyLarge,
            labelStyle = FontStyles.BodyMedium,
            isHighlighted = false,
            showColorDot = false,
            dotColor = Color.Transparent,
            isMultiline = true
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BackgroundSubdued)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
        ) {
            ToolBar(
                text = stringResource(R.string.text_details),
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
                    Icon(
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable {
                                // TODO() need to send request to delete, now just return back
                                onBackClicked()
                            },
                        painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = stringResource(R.string.description_icon_back),
                        tint = Colors.BackgroundPrimary
                    )
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Entry Log: MAY 2, 2024",
                modifier = Modifier.padding(horizontal = 24.dp),
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued,
            )

            Spacer(modifier = Modifier.height(24.dp))

            UniversalDataSection(
                title = null,
                items = mockData,
                footerText = null,
                showProgressBar = false
            )

            Spacer(modifier = Modifier.height(24.dp))
        }


        TextButton(
            text = stringResource(R.string.text_update),
            onClick = {
                // TODO() mock data
                onEditAssetNavigate("ACCT-INV-001")
            },
            color = Colors.Primary,
            textColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
            textStyle = FontStyles.TitleSmall,
        )
    }
}