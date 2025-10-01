package com.fintexinc.dashboard.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.BankingUI
import com.fintexinc.core.data.model.CustomUI
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.data.model.DateValue
import com.fintexinc.core.data.model.InvestmentUI
import com.fintexinc.core.data.model.LiabilityUI
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.data.utils.date.formatToString
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.presentation.ui.datapoint.DataPointUI
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.presentation.ui.widget.ColumnWithBorder
import com.fintexinc.core.presentation.ui.widget.ColumnWithShadow
import com.fintexinc.core.presentation.ui.widget.TabItem
import com.fintexinc.core.presentation.ui.widget.TabsSelector
import com.fintexinc.core.presentation.ui.widget.list.collapsableLazyColumn
import com.fintexinc.core.presentation.ui.widget.modal.AssetLiabilitiesModalBottomSheet
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.TextButton
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R
import com.fintexinc.dashboard.presentation.ui.components.TransactionItemUI
import com.fintexinc.dashboard.presentation.ui.mapper.groupByDate
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
    banking: List<BankingUI>,
    investment: List<InvestmentUI>,
    custom: List<CustomUI>,
    liabilities: List<LiabilityUI>,
    activities: List<Transaction>,
    documents: List<Document>,
    updateCheckedStates: (List<NameValueChecked>, List<NameValueChecked>) -> Unit,
    onAddAssetClicked: (asset: DataPoint?) -> Unit,
    onAddLiabilityClicked: (liability: DataPoint?) -> Unit,
    onOpenJuiceSection: () -> Unit,
    onOpenJuiceArticle: (articleUrl: String) -> Unit,
    onOpenDocumentsClicked: () -> Unit
) {
    val assetsExpanded = remember { mutableStateOf(true) }
    val textAssets = stringResource(R.string.text_assets)
    val textAddAsset = stringResource(R.string.text_add)
    val liabilitiesExpanded = remember { mutableStateOf(true) }
    val textLiabilities = stringResource(R.string.text_liabilities)
    val textAddLiability = stringResource(R.string.title_add_liability)
    val assetsCheckedState =
        banking.map { it.checkedState } + investment.map { it.checkedState } + custom.map { it.checkedState }
    val liabilitiesCheckedState = liabilities.map { it.checkedState }
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
                NetWorthInfoUI(assetsCheckedState, liabilitiesCheckedState, updateCheckedStates)
                Spacer(modifier = Modifier.height(18.dp))
                ProjectionChartUI()
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        collapsableLazyColumn(
            scope = this@LazyColumn,
            dataPoints = assetsCheckedState.map { it.toDataPoint() },
            expanded = assetsExpanded,
            title = textAssets,
            subtitle = assetsCheckedState.sumOf { it.value }.formatCurrency(),
            addItemText = textAddAsset,
            onAddItemClick = {
                onAddAssetClicked(null)
            },
            onItemClick = { dataPoint ->
                onAddAssetClicked(dataPoint)
            }
        )
        collapsableLazyColumn(
            scope = this@LazyColumn,
            dataPoints = liabilitiesCheckedState.map { it.toDataPoint() },
            expanded = liabilitiesExpanded,
            title = textLiabilities,
            subtitle = liabilitiesCheckedState.sumOf { it.value }.formatCurrency(),
            addItemText = textAddLiability,
            isLastList = true,
            onAddItemClick = {
                onAddLiabilityClicked(null)
            },
            onItemClick = { dataPoint ->
                onAddLiabilityClicked(dataPoint)
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
                        contentDescription = stringResource(R.string.description_image_background_),
                    )
                    Image(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp),
                        painter = painterResource(R.drawable.ic_folder),
                        contentDescription = stringResource(R.string.description_icon_folder)
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
                ActivityUI(
                    activities = activities,
                    documents = documents,
                    onOpenDocumentsClicked = onOpenDocumentsClicked
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Spacer(modifier = Modifier.height(18.dp))
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        onOpenJuiceSection()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 22.dp),
                    text = stringResource(R.string.text_articles_you_might_like),
                    style = FontStyles.TitleMediumBold,
                    color = Colors.BrandBlack
                )
                Spacer(
                    modifier = Modifier.width(4.dp)
                )
                Icon(
                    modifier = Modifier
                        .size(28.dp),
                    painter = painterResource(id = com.fintexinc.core.R.drawable.ic_arrow_right),
                    tint = Colors.IconSubtitled,
                    contentDescription = stringResource(R.string.description_icon_open),
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(3) {
                    Row(
                        modifier = Modifier
                            .width(320.dp)
                            .height(128.dp)
                            .background(
                                color = Colors.Background,
                                RoundedCornerShape(16.dp)
                            )
                            .clickableShape(
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                // TODO: add actual info from mocks
                                onOpenJuiceArticle("https://www.tangerine.ca/en/thejuice/invest/what-should-you-do-with-your-cash")
                            },
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .width(128.dp)
                                .fillMaxHeight()
                                .background(
                                    color = Colors.BorderSubdued,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier
                                .width(200.dp)
                                .fillMaxHeight()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
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
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun NetWorthNotificationUI() {
    ColumnWithShadow(
        outerHorizontalPadding = 0.dp,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Image(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 16.dp, top = 16.dp),
                painter = painterResource(R.drawable.ic_welcome_notification),
                contentDescription = stringResource(R.string.description_icon_notification)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = stringResource(R.string.text_welcome_to_tangerine_wealth),
                    color = Colors.TextSubdued,
                    style = FontStyles.BodyMediumBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = stringResource(R.string.text_track_your_net_worth),
                    style = FontStyles.BodyMedium,
                    color = Colors.TextSubdued
                )
            }
            Icon(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 12.dp, end = 12.dp)
                    .align(Alignment.Top),
                painter = painterResource(com.fintexinc.core.R.drawable.ic_close),
                tint = Colors.TextSubdued,
                contentDescription = stringResource(
                    R.string.description_icon_close
                )
            )
        }
    }
}

@Composable
private fun NetWorthInfoUI(
    assets: List<NameValueChecked>,
    liabilities: List<NameValueChecked>,
    updateCheckedStates: (List<NameValueChecked>, List<NameValueChecked>) -> Unit
) {
    val showAccountsBottomSheet = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = stringResource(R.string.text_my_net_worth),
            style = FontStyles.TitleLarge,
            color = Colors.BrandBlack
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                text = (assets.sumOf { it.value } - liabilities.sumOf { it.value }).formatCurrency(),
                style = FontStyles.DisplaySmall,
                color = Colors.TextPrimary,
                maxLines = 1
            )
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .background(
                        color = Colors.BackgroundInteractive,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickableShape(RoundedCornerShape(16.dp)) {
                        showAccountsBottomSheet.value = true
                    }
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = stringResource(R.string.text_all_accounts),
                    style = FontStyles.BodySmall,
                    color = Colors.TextInteractive
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_down),
                    tint = Colors.TextInteractive,
                    contentDescription = stringResource(R.string.description_icon_all_accounts_expand),
                )
            }
        }
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
}

@Composable
private fun ProjectionChartUI() {
    val showAccountsBottomSheet = remember { mutableStateOf(false) }
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
                text = stringResource(R.string.text_projections),
                style = FontStyles.BodyLarge
            )

        }
        Spacer(modifier = Modifier.height(18.dp))
        TangerineProjectionUI()
        Spacer(modifier = Modifier.height(18.dp))
        ChartPeriodSelector(
            onPeriodSelected = {
                // Handle period selection
            }
        )
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
        text = (assets.sumOf { it.value } - liabilities.sumOf { it.value }).formatCurrency(),
        style = FontStyles.DisplaySmall,
        color = Colors.TextPrimary
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
            contentDescription = stringResource(R.string.description_icon_increase),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.wrapContentSize(),
            // Will be added in API
            text = "$200 (1.87%)",
            style = FontStyles.TitleSmall,
            color = Colors.TextSuccess
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
        text = (assets.sumOf { it.value } - liabilities.sumOf { it.value }).formatCurrency(),
        style = FontStyles.DisplaySmall,
        color = Colors.TextPrimary
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
            contentDescription = stringResource(R.string.description_icon_increase),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier.wrapContentSize(),
            // Will be added in API
            text = "$200 (1.87%)",
            style = FontStyles.TitleSmall,
            color = Colors.TextSuccess
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
        legendLabels = Triple(
            stringResource(R.string.text_assets),
            stringResource(R.string.text_liabilities),
            stringResource(R.string.text_my_net_worth)
        ),
        enabledColors = Pair(
            Colors.TransactionIncome,
            Colors.TransactionLiability
        ),
        disabledColors = Pair(
            Colors.BrandBlack,
            Colors.BrandGray
        ),
    )
}

// TODO: refactor. Add logic
@Composable
private fun TangerineProjectionUI() {
    TangerineProjectionsChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(284.dp)
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
        legendLabels = Triple(
            stringResource(R.string.text_increase),
            stringResource(R.string.text_money_out),
            stringResource(R.string.text_net_contributions)
        ),
        radius = 4.dp,
        horizontalIndicatorStep = 400000.0,
        enabledColors = Pair(
            Color(0xFF3E70FF),
            Color(0xFFFFC43F)
        ),
        disabledColors = Pair(
            Colors.BrandGray,
            Colors.BrandBlack
        )
    )
}

@Composable
private fun ActivityUI(
    activities: List<Transaction>,
    documents: List<Document>,
    onOpenDocumentsClicked: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        text = stringResource(R.string.text_recent_investment_activity_and_document),
        style = FontStyles.TitleSmall
    )
    Spacer(modifier = Modifier.height(16.dp))
    TabsSelector(
        tabs = listOf(
            TabItem(
                title = stringResource(R.string.text_activity),
                content = {
                    ActivitiesUI(transactions = activities)
                }
            ),
            TabItem(
                title = stringResource(R.string.text_documents),
                content = {
                    DocumentsUI(
                        dataPoints = documents.map {
                            DataPoint(
                                id = it.id,
                                name = it.documentName,
                                subName = it.documentDate.formatToString(),
                                iconResId = com.fintexinc.core.R.drawable.ic_document
                            )
                        },
                        onOpenDocumentsClicked = onOpenDocumentsClicked
                    )
                }
            )
        )
    )
}

@Composable
private fun ActivitiesUI(transactions: List<Transaction>) {
    val groupedTransactions = remember(transactions) {
        val grouped = transactions.groupByDate()
        grouped
    }

    Spacer(modifier = Modifier.height(18.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        groupedTransactions.forEachIndexed { groupIndex, group ->

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = group.date,
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued
            )

            group.transactions.forEachIndexed { index, transaction ->
                TransactionItemUI(
                    transaction = transaction,
                    onClick = {
                        // TODO() add navigation here
                    }
                )

                if (index < group.transactions.size - 1) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 12.dp)
                            .background(Colors.BorderSubdued)
                    )
                }
            }

            if (group != groupedTransactions.last()) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun DocumentsUI(
    dataPoints: List<DataPoint>,
    onOpenDocumentsClicked: () -> Unit
) {
    Spacer(modifier = Modifier.height(18.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        dataPoints.forEach {
            DataPointUI(
                dataPoint = it,
                isLastItem = dataPoints.last() == it,
                onClick = {
                    onOpenDocumentsClicked()
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        TextButton(
            text = stringResource(R.string.text_see_investment_documents),
            onClick = {

            },
        )
    }
}