package com.fintexinc.dashboard.presentation.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.ItemType
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.PerformanceItem
import com.fintexinc.core.presentation.ui.widget.RowWithShadow
import com.fintexinc.core.presentation.ui.widget.add.ItemTypeSelection
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.components.TextButton
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R
import com.fintexinc.dashboard.presentation.ui.models.AccountUI
import com.fintexinc.dashboard.presentation.ui.widget.chart.TangerinePieChart
import com.tangerine.charts.compose_charts.PerformanceChartUI
import com.tangerine.charts.compose_charts.models.Pie

@Composable
fun MyPortfolioUI(
    accounts: List<Account>,
    performance: List<PerformanceItem>,
    onOpenAccount: (accountId: String) -> Unit
) {
    data class MockItemType(override val label: String) : ItemType

    val mockItemTypes = listOf(
        MockItemType(stringResource(R.string.text_none)),
        MockItemType(stringResource(R.string.text_registered_non_registered)),
        MockItemType(stringResource(R.string.text_account_type)),
    )

    val showInvestmentAccountsSelection = remember {
        mutableStateOf(false)
    }

    val selectedGroupingType = remember {
        mutableStateOf(GroupingType.REGISTERED_STATUS)
    }

    val isShowNewsBanner = remember {
        mutableStateOf(true)
    }
    val selectedFilterType = remember {
        mutableStateOf(mockItemTypes[1])
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (isShowNewsBanner.value) {
            Spacer(modifier = Modifier.height(18.dp))
            RowWithShadow(
                modifier = Modifier.clickable {
                    // TODO() navigate to news
                }
            ) {
                Column(
                    modifier = Modifier.wrapContentHeight(),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Icon(
                        modifier = Modifier.wrapContentSize(),
                        painter = painterResource(R.drawable.ic_pictogram),
                        contentDescription = stringResource(R.string.description_icon_pictogram),
                        tint = Color.Unspecified,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        text = stringResource(R.string.text_new_market_insights),
                        style = FontStyles.BodyMedium,
                        color = Colors.Text,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        text = stringResource(R.string.text_check_in_update),
                        style = FontStyles.BodyMediumBold,
                        color = Colors.TextInteractive,
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { isShowNewsBanner.value = false },
                    painter = painterResource(com.fintexinc.core.R.drawable.ic_close),
                    tint = Colors.IconSubtitled,
                    contentDescription = stringResource(R.string.description_icon_close),
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Charts(performance)
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { showInvestmentAccountsSelection.value = true }
        ) {
            Text(
                modifier = Modifier.wrapContentHeight(),
                text = stringResource(R.string.text_my_accounts),
                style = FontStyles.TitleMediumBold
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Icon(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(com.fintexinc.core.R.drawable.icon_sliders_light),
                tint = Colors.Primary,
                contentDescription = stringResource(R.string.description_icon_filter)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        when (selectedGroupingType.value) {
            GroupingType.NONE -> {
                val allAccountsUI = accounts.map {
                    AccountUI(
                        accountId = it.accountId,
                        name = it.userId,
                        subName = it.accountId,
                        value = it.income.formatCurrency(),
                        valueChange = 832.01,
                        percentageChange = 4.39
                    )
                }

                AccountListUI(
                    title = stringResource(R.string.text_all_accounts),
                    accounts = allAccountsUI,
                    onOpenAccount = onOpenAccount,
                    totalSum = accounts.sumOf { it.income }.formatCurrency(),
                    isRoundedTop = true,
                )
            }

            GroupingType.REGISTERED_STATUS -> {
                val registeredAccounts = accounts.map {
                    AccountUI(
                        accountId = it.accountId,
                        name = it.userId,
                        subName = it.accountId,
                        value = it.income.formatCurrency(),
                        valueChange = 832.01,
                        percentageChange = 4.39,
                    )
                }

                val nonRegisteredAccounts = listOf(
                    AccountUI(
                        accountId = "TFSA-001",
                        name = "Jack Dawson TFSA",
                        subName = "39024242",
                        value = "20,000 CAD",
                        valueChange = 832.01,
                        percentageChange = 4.39,
                    ),
                    AccountUI(
                        accountId = "TFSA-002",
                        name = "Jack Dawson TFSA",
                        subName = "39024242",
                        value = "20,000 CAD",
                        valueChange = 832.01,
                        percentageChange = 4.39,
                    ),
                )

                when (selectedFilterType.value.label) {
                    stringResource(R.string.text_none) -> {
                        val allAccounts = registeredAccounts + nonRegisteredAccounts
                        AccountListUI(
                            title = stringResource(R.string.text_all_accounts),
                            accounts = allAccounts,
                            onOpenAccount = onOpenAccount,
                            totalSum = (accounts.sumOf { it.income } * 2).formatCurrency(),
                            isRoundedTop = true,
                        )
                    }

                    stringResource(R.string.text_registered_non_registered) -> {
                        AccountListUI(
                            title = stringResource(R.string.format_registered_accounts),
                            accounts = registeredAccounts,
                            onOpenAccount = onOpenAccount,
                            totalSum = accounts.sumOf { it.income }.formatCurrency(),
                            isRoundedTop = true,
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 18.dp),
                            color = Colors.BorderSubdued
                        )

                        AccountListUI(
                            title = stringResource(R.string.format_non_registered_accounts),
                            accounts = nonRegisteredAccounts,
                            onOpenAccount = null,
                            totalSum = accounts.sumOf { it.income }.formatCurrency(),
                            isRoundedTop = false,
                        )
                    }

                    stringResource(R.string.text_account_type) -> {
                        // TODO() Mock for ui
                        val tfsaAccounts =
                            registeredAccounts.filter {
                                it.name.contains(
                                    "TFSA",
                                    ignoreCase = true
                                )
                            } +
                                    nonRegisteredAccounts.filter {
                                        it.name.contains(
                                            "TFSA",
                                            ignoreCase = true
                                        )
                                    }

                        if (tfsaAccounts.isNotEmpty()) {
                            AccountListUI(
                                title = "TFSA",
                                accounts = tfsaAccounts,
                                onOpenAccount = onOpenAccount,
                                totalSum = "$650,000",
                                isRoundedTop = true,
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 18.dp),
                                color = Colors.BorderSubdued
                            )
                        }

                        val nonRegAccounts =
                            registeredAccounts.filter {
                                !it.name.contains(
                                    "TFSA",
                                    ignoreCase = true
                                )
                            } +
                                    nonRegisteredAccounts.filter {
                                        !it.name.contains(
                                            "TFSA",
                                            ignoreCase = true
                                        )
                                    }

                        if (nonRegAccounts.isNotEmpty()) {
                            AccountListUI(
                                title = stringResource(R.string.format_non_registered_accounts),
                                accounts = nonRegAccounts,
                                onOpenAccount = null,
                                totalSum = "$25,000",
                                isRoundedTop = true,
                            )
                        }
                    }
                }
            }

            GroupingType.ACCOUNT_TYPE -> {
                val groupedByUser = accounts.groupBy { it.userId }
                var isFirst = true

                groupedByUser.forEach { (userId, userAccounts) ->
                    val accountsUI = userAccounts.map {
                        AccountUI(
                            accountId = it.accountId,
                            name = it.userId,
                            subName = it.accountId,
                            value = it.income.formatCurrency(),
                            valueChange = 832.01,
                            percentageChange = 4.39,
                        )
                    }

                    if (!isFirst) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 18.dp),
                            color = Colors.BorderSubdued,
                        )
                    }

                    AccountListUI(
                        title = userId,
                        accounts = accountsUI,
                        onOpenAccount = onOpenAccount,
                        totalSum = userAccounts.sumOf { it.income }.formatCurrency(),
                        isRoundedTop = isFirst,
                    )

                    isFirst = false
                }

                // TODO() mock data
                val nonRegisteredAccounts = listOf(
                    AccountUI(
                        accountId = "TFSA-001",
                        name = "Jack Dawson TFSA",
                        subName = "39024242",
                        value = "20,000 CAD",
                        valueChange = 832.01,
                        percentageChange = 4.39,
                    ),
                    AccountUI(
                        accountId = "TFSA-002",
                        name = "Jack Dawson TFSA",
                        subName = "39024242",
                        value = "20,000 CAD",
                        valueChange = 832.01,
                        percentageChange = 4.39,
                    ),
                )

                if (nonRegisteredAccounts.isNotEmpty()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 18.dp),
                        color = Colors.BorderSubdued,
                    )

                    AccountListUI(
                        title = stringResource(R.string.format_non_registered_accounts),
                        accounts = nonRegisteredAccounts,
                        onOpenAccount = null,
                        totalSum = accounts.sumOf { it.income }.formatCurrency(),
                        isRoundedTop = false,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            text = stringResource(R.string.title_add_account),
            onClick = {},
            color = Colors.Primary,
            modifier = Modifier.padding(horizontal = 18.dp),
        )

        Spacer(modifier = Modifier.height(30.dp))
        data class FilterItemType(override val label: String, val type: GroupingType) : ItemType

        val textNone = stringResource(R.string.text_none)
        val textRegisterNonReg = stringResource(R.string.text_register_non_reg)
        val textAccountTYpe = stringResource(R.string.text_account_type)

        val filterItemTypes = listOf(
            FilterItemType(textNone, GroupingType.NONE),
            FilterItemType(textRegisterNonReg, GroupingType.REGISTERED_STATUS),
            FilterItemType(textAccountTYpe, GroupingType.ACCOUNT_TYPE),
        )

        if (showInvestmentAccountsSelection.value) {
            ItemTypeSelection(
                itemTypeTitle = stringResource(R.string.text_group_by),
                itemTypes = filterItemTypes,
                onItemTypeSelected = { selectedItem ->
                    if (selectedItem is FilterItemType) {
                        selectedGroupingType.value = selectedItem.type
                    }
                    showInvestmentAccountsSelection.value = false
                },
                onCancel = {
                    showInvestmentAccountsSelection.value = false
                },
            )
        }
    }
}

@Composable
private fun AccountListUI(
    modifier: Modifier = Modifier,
    title: String,
    totalSum: String,
    accounts: List<AccountUI>,
    onOpenAccount: ((accountId: String) -> Unit)? = null,
    isRoundedTop: Boolean = true
) {
    val rowShape = if (isRoundedTop) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    } else {
        RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
    }
    val expanded = remember {
        mutableStateOf(true)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp)
            .background(
                color = Colors.Background,
                shape = rowShape
            )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(shape = rowShape)
                .clickable { expanded.value = !expanded.value }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = FontStyles.BodyLargeBold,
                color = Colors.BrandBlack
            )

            Text(
                modifier = Modifier,
                text = totalSum,
                style = FontStyles.BodyLargeBold,
                color = Colors.BrandBlack
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                modifier = Modifier.rotate(if (expanded.value) 180f else 0f),
                painter = painterResource(com.fintexinc.core.R.drawable.ic_chevron_down),
                tint = Colors.BrandBlack,
                contentDescription = stringResource(R.string.description_icon_expand)
            )
        }

        if (expanded.value) {
            HorizontalDivider(color = Colors.BorderSubdued)
            accounts.forEachIndexed { index, account ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (onOpenAccount != null) {
                                Modifier.clickable {
                                    onOpenAccount(account.accountId)
                                }
                            } else Modifier
                        )
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = account.name,
                            style = FontStyles.BodyLarge,
                            color = Colors.Text
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = account.value,
                                style = FontStyles.BodyLargeBold,
                                color = Colors.Text
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "▲ " + String.format(
                                    "$%.2f (%.2f%%)",
                                    account.valueChange,
                                    account.percentageChange
                                ),
                                style = FontStyles.BodyMedium,
                                color = Colors.TextSuccess
                            )
                        }
                    }

                    if (onOpenAccount != null) {
                        Icon(
                            modifier = Modifier.wrapContentSize(),
                            painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
                            tint = Colors.IconSupplementary,
                            contentDescription = stringResource(R.string.description_view_details)
                        )
                    }
                }

                if (index < accounts.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = Colors.BorderSubdued,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun Charts(performance: List<PerformanceItem>) {
    val pageCount = 4
    val pagerState = rememberPagerState(
        initialPage = (Int.MAX_VALUE / 2 / pageCount) * pageCount,
        pageCount = { Int.MAX_VALUE }
    )

    HorizontalPager(state = pagerState) { page ->
        val actualPage = page % pageCount

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp)
                .background(
                    color = Colors.Background,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(18.dp)
        ) {
            when (actualPage) {
                0 -> PerformanceChartUI(
                    title = stringResource(R.string.text_investor_performance),
                    performance = performance
                )
                1 -> AssetMixChartUI()
                2 -> SectionExposureChartUI()
                3 -> GeographicExposure()
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(pageCount) { iteration ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .then(
                                if (actualPage == iteration) {
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
private fun SectionExposureChartUI() {
    TangerinePieChart(
        title = stringResource(R.string.text_sector_exposure),
        pieData = listOf(
            Pie("Technology", 50.0, color = Color(0xFF0D7C75)),
            Pie("Finance", 10.0, color = Color(0xFFFEAC5B)),
            Pie("Healthcare", 20.0, color = Color(0xFFEC407A))
        )
    )
}

@Composable
private fun AssetMixChartUI() {
    TangerinePieChart(
        title = stringResource(R.string.text_asset_mix),
        pieData = listOf(
            Pie("Stocks", 40.0, color = Color(0xFFE57373)),
            Pie("Bonds", 40.0, color = Color(0xFF880E4F)),
            Pie("Futures", 20.0, color = Colors.BackgroundPrimary),
        )
    )
}

@Composable
private fun GeographicExposure() {
    TangerinePieChart(
        title = stringResource(R.string.text_geographic_exposure),
        pieData = listOf(
            Pie("North America", 65.0, color = Color(0xFF075BBC)),
            Pie("Europe", 35.0, color = Color(0xFFEC407A)),
        )
    )
}

@Composable
private fun TopHoldingsItem(
    holdingsName: String,
    holdingsSubName: String,
    sum: String,
    percent: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = holdingsName,
                style = FontStyles.BodyLarge,
                color = Colors.Text,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = holdingsSubName,
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.wrapContentWidth(),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = sum,
                style = FontStyles.BodyLargeBold,
                color = Colors.Text,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = percent,
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued,
            )
        }
    }
}

enum class GroupingType(label: String) {
    NONE("None"),
    REGISTERED_STATUS("Registered/Non-Registered"),
    ACCOUNT_TYPE("AccountType")
}