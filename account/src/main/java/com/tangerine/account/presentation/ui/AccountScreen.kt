package com.tangerine.account.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.widget.TabItem
import com.fintexinc.core.presentation.ui.widget.TabsSelector
import com.fintexinc.core.presentation.ui.widget.ToolBar
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
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
    navigateToTransactionDetailScreen: () -> Unit,
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
                navigateToTransactionDetailScreen = navigateToTransactionDetailScreen,
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
    navigateToTransactionDetailScreen: () -> Unit,
) {
    val selectedTab = remember {
        mutableStateOf(AccountTab.SUMMARY)
    }

    val showBottomSheet = state is AccountViewModel.State.Activities ||
            state is AccountViewModel.State.Documents ||
            state is AccountViewModel.State.Positions ||
            state is AccountViewModel.State.Summary

    if (showBottomSheet) {
        val bottomSheetState = rememberBottomSheetScaffoldState()

        BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContent = {
                BottomSheetTabsContent(
                    bottomSheetState = bottomSheetState,
                    navigateToTransactionDetailScreen = navigateToTransactionDetailScreen,
                )
            },
            sheetPeekHeight = 120.dp,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContainerColor = Colors.Background,
            sheetShadowElevation = 16.dp,
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
                        .clickable {}
                )
            }
        )
        AccountBalanceCard(
            balance = "$28,230.00",
            portfolioType = "Balanced Core Portfolio",
            maskedAccountNumber = "***1234"
        )

        AccountTabsUI(
            selectedTab,
            onTabSelected = { tab ->
                onTabSelected(tab)
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (state) {
            is AccountViewModel.State.Activities -> ActivityUI(state.data)
            is AccountViewModel.State.Documents -> DocumentsUI(state.data, onOpenDocuments)
            is AccountViewModel.State.Positions -> PositionsUI(state.data)
            is AccountViewModel.State.Summary -> SummaryUI(state.data)
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetTabsContent(
    bottomSheetState: BottomSheetScaffoldState,
    navigateToTransactionDetailScreen: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    TabsSelector(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 28.dp),
        tabs = listOf(
            TabItem(
                title = stringResource(R.string.title_transactions),
                content = {
                    TransactionsUi()
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
                        navigateToTransactionDetailScreen = navigateToTransactionDetailScreen
                    )
                },
                onTabSelected = {
                    scope.launch { bottomSheetState.bottomSheetState.expand() }
                }
            )
        )
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
    val tabs = listOf(
        AccountTab.SUMMARY,
        AccountTab.POSITIONS,
        AccountTab.ACTIVITY,
        AccountTab.DOCUMENTS
    )
    onTabSelected(selectedTab.value)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val offset = LocalDensity.current.run { 16.dp.toPx() }
        tabs.forEach { tab ->
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .then(
                        if (selectedTab.value == tab) {
                            Modifier.drawBehind {
                                val strokeWidth = 3.dp.toPx()
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    color = Colors.BackgroundPrimary,
                                    start = Offset(offset / 2, y),
                                    end = Offset(size.width - offset / 2, y),
                                    strokeWidth = strokeWidth
                                )
                            }
                        } else {
                            Modifier
                        }
                    )
                    .clickable {
                        selectedTab.value = tab
                    }
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                text = tab.label,
                style = FontStyles.BodySmallBold,
                color = Colors.BrandBlack
            )
        }
    }
}

enum class AccountTab(val label: String) {
    SUMMARY("Summary"),
    POSITIONS("Positions"),
    ACTIVITY("Activity"),
    DOCUMENTS("Documents")
}