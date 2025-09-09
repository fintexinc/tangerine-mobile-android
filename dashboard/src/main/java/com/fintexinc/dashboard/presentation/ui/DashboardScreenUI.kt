package com.fintexinc.dashboard.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.widget.TwoTabsSelector
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R
import com.fintexinc.dashboard.presentation.ui.screen.MyNetWorthUI
import com.fintexinc.dashboard.presentation.ui.screen.MyPortfolioUI
import com.fintexinc.dashboard.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreenUI(
    state: DashboardViewModel.State,
    onPlatformClicked: () -> Unit,
    onOpenAccount: () -> Unit,
    updateCheckedStates: (List<NameValueChecked>, List<Boolean>, List<NameValueChecked>, List<Boolean>) -> Unit
) {
    when (state) {
        is DashboardViewModel.State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DashboardViewModel.State.Data -> Content(state, onPlatformClicked, onOpenAccount,
            updateCheckedStates)
    }
}

@Composable
private fun Content(
    state: DashboardViewModel.State.Data,
    onPlatformClicked: () -> Unit,
    onOpenAccount:() -> Unit,
    updateCheckedStates: (List<NameValueChecked>, List<Boolean>, List<NameValueChecked>, List<Boolean>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.BackgroundSubdued)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 18.dp),
            text = stringResource(R.string.app_title),
            style = FontStyles.HeadingRegular,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .shadow(2.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        TwoTabsSelector(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 18.dp, top = 12.dp, end = 18.dp),
            firstTabTitle = stringResource(R.string.text_my_net_worth),
            secondTabTitle = stringResource(R.string.text_my_portfolio),
            onFirstTabSelected = {
                MyNetWorthUI(
                    assets = state.assets,
                    liabilities = state.liabilities,
                    activities = state.activities,
                    documents = state.documents,
                    updateCheckedStates = updateCheckedStates
                )
            },
            onSecondTabSelected = {
                MyPortfolioUI(
                    accounts = state.accounts,
                    onOpenAccount = onOpenAccount
                )
            }
        )
    }
}