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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.presentation.ui.tab.ActivityUI
import com.tangerine.account.presentation.ui.tab.DocumentsUI
import com.tangerine.account.presentation.ui.tab.PositionsUI
import com.tangerine.account.presentation.ui.tab.SummaryUI
import com.tangerine.account.presentation.viewmodel.AccountViewModel

@Composable
fun AccountScreen(
    state: AccountViewModel.State,
    onBackClicked: () -> Unit,
    onOpenDocuments: () -> Unit
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
                onOpenDocuments = onOpenDocuments
            )
        }
    }
}

@Composable
private fun Content(
    state: AccountViewModel.State,
    onBackClicked: () -> Unit,
    onOpenDocuments: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BackgroundSubdued)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Colors.BackgroundPrimary)
                .padding(vertical = 12.dp, horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        onBackClicked()
                    },
                painter = painterResource(com.fintexinc.core.R.drawable.ic_back_arrow),
                contentDescription = "Back Icon",
                tint = Colors.Background,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                text = "Jack Dawson TFSA\n79127912",
                style = FontStyles.TitleMediumMedium,
                color = Colors.Background,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(com.fintexinc.core.R.drawable.ic_edit),
                contentDescription = "Edit Icon",
                tint = Colors.Background,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        val selectedTab = remember {
            mutableStateOf(AccountTab.SUMMARY)
        }
        AccountTabsUI(
            selectedTab,
            onTabSelected = { tab ->
                selectedTab.value = tab
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