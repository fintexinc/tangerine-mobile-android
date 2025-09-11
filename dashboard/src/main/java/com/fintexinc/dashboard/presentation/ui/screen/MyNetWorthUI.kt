package com.fintexinc.dashboard.presentation.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.DateValue
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.data.utils.date.formatToString
import com.fintexinc.core.domain.model.DataPoint
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.presentation.ui.datapoint.DataPointUI
import com.fintexinc.core.presentation.ui.widget.ColumnWithBorder
import com.fintexinc.core.presentation.ui.widget.ColumnWithShadow
import com.fintexinc.core.presentation.ui.widget.TwoTabsSelector
import com.fintexinc.core.presentation.ui.widget.list.collapsableLazyColumn
import com.fintexinc.core.presentation.ui.widget.modal.AssetLiabilitiesModalBottomSheet
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R
import com.fintexinc.dashboard.presentation.ui.mapper.toDataPoint
import com.fintexinc.dashboard.presentation.ui.widget.chart.ChartPeriodSelector
import com.fintexinc.dashboard.presentation.ui.widget.chart.NegativeValuesPosition
import com.fintexinc.dashboard.presentation.ui.widget.chart.Period
import com.fintexinc.dashboard.presentation.ui.widget.chart.TangerineBarChart
import com.tangerine.charts.compose_charts.TangerineProjectionsChart
import com.tangerine.charts.compose_charts.extensions.format
import com.tangerine.charts.compose_charts.models.AnimationMode
import com.tangerine.charts.compose_charts.models.DividerProperties
import com.tangerine.charts.compose_charts.models.DotProperties
import com.tangerine.charts.compose_charts.models.DrawStyle
import com.tangerine.charts.compose_charts.models.GridProperties
import com.tangerine.charts.compose_charts.models.HorizontalIndicatorProperties
import com.tangerine.charts.compose_charts.models.IndicatorCount
import com.tangerine.charts.compose_charts.models.LabelHelperProperties
import com.tangerine.charts.compose_charts.models.LabelProperties
import com.tangerine.charts.compose_charts.models.Line
import com.tangerine.charts.compose_charts.models.PopupProperties
import com.tangerine.charts.compose_charts.models.StrokeStyle

@Composable
fun MyNetWorthUI(
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>,
    activities: List<Transaction>,
    documents: List<Document>,
    updateCheckedStates: (List<NameValueChecked>, List<Boolean>, List<NameValueChecked>, List<Boolean>) -> Unit
) {
    val assetsExpanded = remember { mutableStateOf(true) }
    val textAssets = stringResource(R.string.text_assets)
    val textAddAsset = stringResource(R.string.text_add_asset)
    val liabilitiesExpanded = remember { mutableStateOf(true) }
    val textLiabilities = stringResource(R.string.text_liabilities)
    val textAddLiability = stringResource(R.string.text_add_liability)
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 18.dp, end = 18.dp, top = 18.dp)
            ) {
                NetWorthNotificationUI()
                Spacer(modifier = Modifier.height(18.dp))
                NetWorthInfoUI(assets, liabilities, updateCheckedStates = updateCheckedStates)
            }
        }
        collapsableLazyColumn(
            scope = this@LazyColumn,
            dataPoints = assets.map { it.toDataPoint() },
            expanded = assetsExpanded,
            title = textAssets,
            subtitle = assets.sumOf { it.value }.formatCurrency(),
            addItemText = textAddAsset,
            onAddItemClick = {
                // TODO: add logic
            }
        )
        collapsableLazyColumn(
            scope = this@LazyColumn,
            dataPoints = liabilities.map { it.toDataPoint() },
            expanded = liabilitiesExpanded,
            title = textLiabilities,
            subtitle = liabilities.sumOf { it.value }.formatCurrency(),
            addItemText = textAddLiability,
            isLastList = true,
            onAddItemClick = {
                // TODO: add logic
            }
        )
        item {
            Spacer(modifier = Modifier.height(18.dp))
            ColumnWithShadow {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 40.dp
                        ),
                    text = stringResource(R.string.text_you_saved),
                    style = FontStyles.BodyLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            start = 16.dp,
                            end = 40.dp,
                            bottom = 16.dp
                        ),
                    text = stringResource(R.string.text_money_stays),
                    style = FontStyles.BodyMedium,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(end = 20.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        painter = painterResource(R.drawable.ic_saved_background),
                        contentDescription = "Background"
                    )
                    Image(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp),
                        painter = painterResource(R.drawable.ic_folder),
                        contentDescription = "Folder Icon"
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(18.dp))
        }
        item {
            ColumnWithBorder {
                Spacer(modifier = Modifier.height(18.dp))
                ActivityUI(activities, documents)
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Spacer(modifier = Modifier.height(18.dp))
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 22.dp),
                text = stringResource(R.string.text_articles_you_might_like),
                style = FontStyles.TitleMediumBold,
                color = Colors.BrandBlack
            )
        }
        items(3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
                    .padding(horizontal = 18.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(color = Colors.BackgroundSubdued, RoundedCornerShape(16.dp)),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.5F)
                        .fillMaxHeight()
                        .background(color = Colors.BorderSubdued)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Column(
                    modifier = Modifier
                        .weight(0.5F)
                        .fillMaxHeight()
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        text = stringResource(R.string.text_invest),
                        style = FontStyles.BodySmall,
                        color = Colors.TextSave
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = stringResource(R.string.text_invest_instructions),
                        style = FontStyles.HeadingMedium,
                        color = Colors.BrandBlack
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 18.dp)
                    .background(
                        color = Colors.Background,
                        shape = RoundedCornerShape(40.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Colors.BorderSubdued,
                        shape = RoundedCornerShape(40.dp)
                    )
                    .clickable {

                    }
                    .padding(vertical = 12.dp),
                text = stringResource(R.string.text_show_more),
                textAlign = TextAlign.Center,
                style = FontStyles.HeadingLarge,
                color = Colors.BrandBlack
            )
        }
        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun NetWorthNotificationUI() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Colors.BackgroundSecondary, RoundedCornerShape(bottomStart = 16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Icon(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(R.drawable.ic_bell),
                tint = Colors.Background,
                contentDescription = "Notification icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = stringResource(R.string.text_well_done),
                color = Colors.TextInverse,
                style = FontStyles.BodyLargeBold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = stringResource(R.string.text_you_increased_rrsp),
            style = FontStyles.BodyLarge,
            color = Colors.TextInverse
        )
    }
}

@Composable
private fun NetWorthInfoUI(
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>,
    updateCheckedStates: (List<NameValueChecked>, List<Boolean>, List<NameValueChecked>, List<Boolean>) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val showAccountsBottomSheet = remember { mutableStateOf(false) }
    HorizontalPager(pagerState) { pageIndex ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Colors.Background,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Colors.BorderSubdued,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        showAccountsBottomSheet.value = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = when (pageIndex) {
                        0 -> stringResource(R.string.text_my_net_worth)
                        1 -> "Projections"
                        2 -> "Cache Flow"
                        else -> stringResource(R.string.text_my_net_worth)
                    },
                    style = FontStyles.BodyLarge
                )
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = stringResource(R.string.text_all_accounts),
                    style = FontStyles.BodySmall,
                    color = Colors.TextInteractive
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_down),
                    tint = Color(0xFF006FD6),
                    contentDescription = "All Accounts Open Icon",
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            when (pageIndex) {
                0 -> TangerineNetWorthUI(assets, liabilities)
                1 -> {
                    TangerineProjectionUI()
                    Spacer(modifier = Modifier.height(18.dp))
                    ChartPeriodSelector(
                        onPeriodSelected = {
                            // Handle period selection
                        }
                    )
                }

                2 -> ChangesToNetWorthUI(assets, liabilities)
            }
            if (showAccountsBottomSheet.value) {
                AssetLiabilitiesModalBottomSheet(
                    title = stringResource(R.string.text_accounts),
                    isShowing = showAccountsBottomSheet,
                    assets = assets,
                    liabilities = liabilities,
                    updateCheckedStates = updateCheckedStates
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .then(
                                if (pagerState.currentPage == iteration) {
                                    Modifier.background(Colors.BackgroundPrimary)
                                } else {
                                    Modifier
                                        .border(
                                            1.dp, Colors.BackgroundPrimary, CircleShape
                                        )
                                        .animateContentSize(tween(1000))
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TangerineNetWorthUI(
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>
) {
    val period = remember { mutableStateOf<Period>(Period.SIX_MONTHS) }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp),
        text = "$600,000.00",
        style = FontStyles.DisplaySmall,
        color = Colors.Text
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_up),
            tint = Color(0xFF43A047),
            contentDescription = "Increased Icon"
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.wrapContentSize(),
            text = "$2,000.00",
            style = FontStyles.TitleSmall
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.wrapContentSize(),
            text = stringResource(R.string.text_earned_this_month),
            style = FontStyles.BodyMedium,
            color = Colors.TextSubdued
        )
    }
    Spacer(modifier = Modifier.height(18.dp))
    NetWorthChartUI(assets, liabilities, period.value)
    Spacer(modifier = Modifier.height(18.dp))
    ChartPeriodSelector(
        onPeriodSelected = {
            period.value = it
        }
    )
}

@Composable
private fun ChangesToNetWorthUI(
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>
) {
    val period = remember { mutableStateOf<Period>(Period.SIX_MONTHS) }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp),
        text = "$600,000.00",
        style = FontStyles.DisplaySmall,
        color = Colors.Text
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_up),
            tint = Color(0xFF43A047),
            contentDescription = "Increased Icon"
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.wrapContentSize(),
            text = "$2,000.00",
            style = FontStyles.TitleSmall
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.wrapContentSize(),
            text = stringResource(R.string.text_earned_this_month),
            style = FontStyles.BodyMedium,
            color = Colors.TextSubdued
        )
    }
    Spacer(modifier = Modifier.height(18.dp))
    NetWorthChangesChartUI(assets, liabilities, period.value)
    Spacer(modifier = Modifier.height(18.dp))
    ChartPeriodSelector(
        onPeriodSelected = {
            period.value = it
        }
    )
}

@Composable
private fun NetWorthChartUI(
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>,
    period: Period
) {
    TangerineBarChart(
        negativeValuesPosition = NegativeValuesPosition.REGULAR,
        period = period,
        data = Pair(
            assets.filter { it.isChecked }.map {
                DateValue(
                    date = it.date,
                    value = it.value
                )
            },
            liabilities.filter { it.isChecked }.map {
                DateValue(
                    date = it.date,
                    value = it.value
                )
            }
        ),
        enabledColors = Pair(
            SolidColor(Colors.TransactionIncome),
            SolidColor(Colors.TransactionLiability)
        ),
        disabledColors = Pair(
            SolidColor(Colors.BrandBlack),
            SolidColor(Colors.BrandGray)
        ),
    )
}

// TODO: refactor. Add logic
@Composable
private fun TangerineProjectionUI() {
    TangerineProjectionsChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(244.dp)
            .padding(horizontal = 18.dp),
        data = remember {
            listOf(
                Line(
                    "Today",
                    listOf(600000.0, 800000.0, 1000000.0),
                    dotProperties = DotProperties(enabled = true),
                    drawStyle = DrawStyle.Stroke(2.dp),
                    color = SolidColor(Color(0xFF12B76A)),
                    curvedEdges = false
                ),
                Line(
                    "Five Years",
                    listOf(600000.0, 650000.0, 750000.0),
                    color = SolidColor(Colors.BackgroundPrimary),
                    drawStyle = DrawStyle.Stroke(
                        strokeStyle = StrokeStyle.Dashed(
                            intervals = floatArrayOf(
                                30f,
                                30f
                            ), phase = 25f
                        )
                    ),
                    curvedEdges = false
                )
            )
        },
        minValue = 600000.0,
        maxValue = 1000000.0,
        indicatorProperties = HorizontalIndicatorProperties(
            count = IndicatorCount.StepBased(
                200000.0
            ), contentBuilder = {
                if (it == 1000000.0) {
                    return@HorizontalIndicatorProperties "$1M"
                }
                "$" + (it / 1000).format(0) + "K"
            },
            indicators = listOf(1000000.0, 900000.0, 800000.0, 700000.0, 600000.0)
        ),
        gridProperties = GridProperties(
            xAxisProperties = GridProperties.AxisProperties(
                style = StrokeStyle.Dashed(
                    intervals = floatArrayOf(
                        30f,
                        30f
                    ), phase = 25f
                )
            ),
            yAxisProperties = GridProperties.AxisProperties(enabled = false)
        ),
        animationMode = AnimationMode.Together({ 0 }),
        labelProperties = LabelProperties(
            enabled = true,
            labels = listOf("Today", "5 Years", "10 Years")
        ),
        animationDelay = 0,
        labelHelperProperties = LabelHelperProperties(false),
        dividerProperties = DividerProperties(false),
        popupProperties = PopupProperties(
            textStyle = FontStyles.BodySmall.copy(color = Colors.TextSubdued),
            containerColor = Colors.Background,
            mode = PopupProperties.Mode.PointMode(),
            contentVerticalPadding = 10.dp,
            contentHorizontalPadding = 12.dp
        )
    )
}

@Composable
private fun NetWorthChangesChartUI(
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>,
    period: Period
) {
    TangerineBarChart(
        negativeValuesPosition = NegativeValuesPosition.REVERSED,
        period = period,
        data = Pair(
            assets.filter { it.isChecked }.map {
                DateValue(
                    date = it.date,
                    value = it.value
                )
            },
            liabilities.filter { it.isChecked }.map {
                DateValue(
                    date = it.date,
                    value = it.value * -1F
                )
            }
        ),
        enabledColors = Pair(
            SolidColor(Color(0xFF3E70FF)),
            SolidColor(Color(0xFFFFC43F))
        ),
        disabledColors = Pair(
            SolidColor(Colors.BrandGray),
            SolidColor(Colors.BrandBlack)
        )
    )
}

@Composable
private fun ActivityUI(activities: List<Transaction>, documents: List<Document>) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        text = stringResource(R.string.text_what_is_going_on),
        style = FontStyles.TitleSmall
    )
    TwoTabsSelector(
        firstTabTitle = stringResource(R.string.text_activity),
        secondTabTitle = stringResource(R.string.text_documents),
        onFirstTabSelected = {
            ActivitiesUI(
                activities.map {
                    DataPoint(
                        name = it.investmentDetails.fundName,
                        subName = it.transactionDescription,
                        value = it.transactionAmount.formatCurrency(),
                    )
                }
            )
        },
        onSecondTabSelected = {
            DocumentsUI(
                documents.map {
                    DataPoint(
                        name = it.documentName,
                        subName = it.documentDate.formatToString()
                    )
                }
            )
        }
    )
}

@Composable
private fun ActivitiesUI(dataPoints: List<DataPoint>) {
    Spacer(modifier = Modifier.height(18.dp))
    dataPoints.forEach {
        DataPointUI(
            dataPoint = it,
            isLastItem = dataPoints.last() == it
        )
    }
}

@Composable
private fun DocumentsUI(dataPoints: List<DataPoint>) {
    Spacer(modifier = Modifier.height(18.dp))
    dataPoints.forEach {
        DataPointUI(
            dataPoint = it,
            isLastItem = dataPoints.last() == it
        )
    }
}