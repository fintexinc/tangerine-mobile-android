package com.tangerine.account.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.presentation.ui.widget.TabItem
import com.fintexinc.core.presentation.ui.widget.TabsSelector
import com.fintexinc.core.presentation.ui.widget.ToolBar
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.core.ui.utils.ScreenUtils.GetPercentageOfScreenHeight
import com.tangerine.account.R
import com.tangerine.account.presentation.models.TransactionGroup
import com.tangerine.account.presentation.ui.bottom_tab.DetailsUi
import com.tangerine.account.presentation.ui.bottom_tab.DocumentsUi
import com.tangerine.account.presentation.ui.bottom_tab.TransactionsUi
import com.tangerine.account.presentation.ui.tab.ActivityUI
import com.tangerine.account.presentation.ui.tab.DocumentsUI
import com.tangerine.account.presentation.ui.tab.PositionsUI
import com.tangerine.account.presentation.ui.tab.SummaryUI
import com.tangerine.account.presentation.viewmodel.AccountViewModel
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    state: AccountViewModel.State,
    onBackClicked: () -> Unit,
    onOpenDocuments: () -> Unit,
    onTabSelected: (AccountTab) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchDocumentQueryChanged: (String) -> Unit,
) {
    when (state) {
        is AccountViewModel.State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        else -> {
            Content(
                state = state,
                onBackClicked = onBackClicked,
                onOpenDocuments = onOpenDocuments,
                onTabSelected = onTabSelected,
                onSearchQueryChanged = onSearchQueryChanged,
                onSearchDocumentQueryChanged = onSearchDocumentQueryChanged,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: AccountViewModel.State,
    onBackClicked: () -> Unit,
    onOpenDocuments: () -> Unit,
    onTabSelected: (AccountTab) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchDocumentQueryChanged: (String) -> Unit,
) {
    val selectedTab = remember {
        mutableStateOf(AccountTab.BUY_FUNDS)
    }

    val showBottomSheet = state is AccountViewModel.State.Loaded

    if (showBottomSheet) {
        val bottomSheetState = rememberBottomSheetScaffoldState()

        BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContent = {
                BottomSheetTabsContent(
                    bottomSheetState = bottomSheetState,
                    onSearchQueryChanged = onSearchQueryChanged,
                    searchText = state.mainState.bottomSheet.transactions.query,
                    settledGroups = state.mainState.bottomSheet.transactions.settledGroups,
                    pendingGroups = state.mainState.bottomSheet.transactions.pendingGroups,
                    onSearchDocumentQueryChanged = onSearchDocumentQueryChanged,
                    documentSearchQuery = state.mainState.bottomSheet.documents.query,
                    documents = state.mainState.bottomSheet.documents.filtered,
                )
            },
            sheetPeekHeight = 84.dp,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContainerColor = Colors.Background,
            sheetShadowElevation = 16.dp,
            sheetDragHandle = {
                Surface(
                    modifier =
                        Modifier.padding(top = 8.dp, bottom = 14.dp),
                    color = Colors.BorderSubdued,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(Modifier.size(width = 24.dp, height = 4.dp))
                }
            }
        ) {
            MainPageContent(
                state = state,
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                onBackClicked = onBackClicked,
                onOpenDocuments = onOpenDocuments,
            )
        }
    } else {
        MainPageContent(
            state = state,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            onBackClicked = onBackClicked,
            onOpenDocuments = onOpenDocuments,
        )
    }
}

@Composable
private fun MainPageContent(
    state: AccountViewModel.State,
    selectedTab: MutableState<AccountTab>,
    onTabSelected: (AccountTab) -> Unit,
    onBackClicked: () -> Unit,
    onOpenDocuments: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BackgroundSubdued)
            .verticalScroll(rememberScrollState())
    ) {
        ToolBar(
            text = "Jack Dawson TFSA",
            leftIcon = {
                Icon(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            onBackClicked()
                        },
                    painter = painterResource(com.fintexinc.core.R.drawable.ic_back_arrow),
                    contentDescription = stringResource(R.string.description_icon_navigate_back),
                    tint = Colors.Primary,
                )
            },
            rightIcon = {
                Icon(
                    painter = painterResource(com.fintexinc.core.R.drawable.icon_dots),
                    contentDescription = stringResource(R.string.description_icon_navigate_edit),
                    tint = Colors.Primary,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {},
                )
            }
        )
        AccountBalanceCard(
            balance = "$28,230.00",
            portfolioType = "Balanced Core Portfolio",
            maskedAccountNumber = "***1234",
        )

        Spacer(modifier = Modifier.height(24.dp))

        AccountTabsUI(
            selectedTab,
            onTabSelected = { tab ->
                onTabSelected(tab)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state is AccountViewModel.State.Loaded) {
            when (state.mainState.selectedTab) {
                AccountViewModel.TopTab.SUMMARY -> {
                    SummaryUI(
                        account = state.mainState.summary,
                        performanceData = state.mainState.performanceData,
                        returnsData = state.mainState.returnsItems,
                        holdingsData = state.mainState.holdingsItems,
                    )
                }

                AccountViewModel.TopTab.POSITIONS -> {
                    PositionsUI(positions = state.mainState.positions)
                }

                AccountViewModel.TopTab.ACTIVITIES -> {
                    ActivityUI(data = state.mainState.activities)
                }

                AccountViewModel.TopTab.DOCUMENTS -> {
                    DocumentsUI(
                        documents = state.mainState.documents.all,
                        onOpenDocuments = onOpenDocuments,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetTabsContent(
    bottomSheetState: BottomSheetScaffoldState,
    onSearchQueryChanged: (String) -> Unit,
    searchText: String,
    settledGroups: List<TransactionGroup>,
    pendingGroups: List<TransactionGroup>,
    onSearchDocumentQueryChanged: (String) -> Unit,
    documentSearchQuery: String,
    documents: List<DataPoint>,
) {
    val scope = rememberCoroutineScope()
    val tabsContentMaxHeight = GetPercentageOfScreenHeight(0.85f)
    TabsSelector(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 28.dp),
        tabs = listOf(
            TabItem(
                title = stringResource(R.string.title_transactions),
                content = {
                    TransactionsUi(
                        onSearchQueryChanged = onSearchQueryChanged,
                        settledGroups = settledGroups,
                        pendingGroups = pendingGroups,
                        searchText = searchText,
                    )
                },
                onTabSelected = {
                    scope.launch { bottomSheetState.bottomSheetState.expand() }
                }
            ),
            TabItem(
                title = stringResource(R.string.title_details),
                content = {
                    DetailsUi()
                },
                onTabSelected = {
                    scope.launch { bottomSheetState.bottomSheetState.expand() }
                }
            ),
            TabItem(
                title = stringResource(R.string.title_documents),
                content = {
                    DocumentsUi(
                        searchQuery = documentSearchQuery,
                        onSearchQueryChanged = onSearchDocumentQueryChanged,
                        documents = documents,
                    )
                },
                onTabSelected = {
                    scope.launch { bottomSheetState.bottomSheetState.expand() }
                }
            )
        ),
        contentMaxHeight = tabsContentMaxHeight
    )
}

@Composable
private fun AccountBalanceCard(
    balance: String,
    portfolioType: String,
    maskedAccountNumber: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Colors.Background, shape = RoundedCornerShape(bottomEnd = 16.dp))
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = balance,
            textAlign = TextAlign.Center,
            color = Colors.BrandBlack,
            style = FontStyles.DisplaySmall,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "$portfolioType $maskedAccountNumber",
            color = Colors.BrandBlack,
            textAlign = TextAlign.Center,
            style = FontStyles.TitleSmall,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AccountTabsUI(
    selectedTab: MutableState<AccountTab>,
    onTabSelected: @Composable (AccountTab) -> Unit = {}
) {
    onTabSelected(selectedTab.value)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AccountTab(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.text_buy_funds),
            icon = R.drawable.ic_funds_down,
            onClick = {
                selectedTab.value = AccountTab.BUY_FUNDS
            },
        )

        AccountTab(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.text_sell_funds),
            icon = R.drawable.ic_funds_up,
            onClick = {
                selectedTab.value = AccountTab.SELL_FUNDS
            },
        )

        AccountTab(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.text_automatic_purchases),
            icon = R.drawable.ic_automatic_purchases,
            onClick = {
                selectedTab.value = AccountTab.AUTOMATIC_PURCHASES
            },
        )

        AccountTab(
            label = stringResource(R.string.text_switch_portfolio),
            icon = R.drawable.ic_switch_portfolio,
            onClick = {
                selectedTab.value = AccountTab.SWITCH_PORTFOLIO
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AccountTab(
    modifier: Modifier = Modifier,
    label: String,
    icon: Int,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false
                )
                .background(color = Colors.Background, shape = RoundedCornerShape(12.dp))
                .clickableShape(shape = RoundedCornerShape(12.dp), onClick = { onClick() })
                .padding(12.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = label,
                tint = Colors.Primary,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            color = Colors.TextSubdued,
            style = FontStyles.BodySmall,
            maxLines = 2,
            textAlign = TextAlign.Center,
        )
    }
}

enum class AccountTab(val label: String) {
    BUY_FUNDS("Buy_Funds"),
    SELL_FUNDS("Sell_Funds"),
    AUTOMATIC_PURCHASES("Automatic_Purchases"),
    SWITCH_PORTFOLIO("Switch_Portfolio")
}