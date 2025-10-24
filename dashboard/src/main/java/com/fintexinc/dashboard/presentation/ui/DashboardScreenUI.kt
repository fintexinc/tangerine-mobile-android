package com.fintexinc.dashboard.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.domain.model.Document
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.presentation.ui.widget.TabItem
import com.fintexinc.core.presentation.ui.widget.TabsSelector
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
    onOpenAccountClicked: (accountId: String) -> Unit,
    onAddAssetClicked: (DataPoint?) -> Unit,
    onAddLiabilityClicked: (DataPoint?) -> Unit,
    onOpenJuiceArticle: (url: String) -> Unit,
    onOpenJuiceSection: () -> Unit,
    onSeeInvestmentDocumentClicked: () -> Unit,
    updateCheckedStates: (List<NameValueChecked>, List<NameValueChecked>) -> Unit,
    onActivitiesClicked: (Transaction) -> Unit,
    onOpenDocument: (Document) -> Unit = {},
) {
    when (state) {
        is DashboardViewModel.State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DashboardViewModel.State.Data -> Content(
            state = state,
            onOpenAccount = onOpenAccountClicked,
            onAddAssetClicked = onAddAssetClicked,
            onAddLiabilityClicked = onAddLiabilityClicked,
            onOpenJuiceArticle = onOpenJuiceArticle,
            onOpenJuiceSection = onOpenJuiceSection,
            onSeeInvestmentDocumentClicked = onSeeInvestmentDocumentClicked,
            updateCheckedStates = updateCheckedStates,
            onActivitiesClicked = onActivitiesClicked,
            onOpenDocument = onOpenDocument
        )
    }
}

@Composable
private fun Content(
    state: DashboardViewModel.State.Data,
    onOpenAccount: (accountId: String) -> Unit,
    onAddAssetClicked: (dataPoint: DataPoint?) -> Unit,
    onAddLiabilityClicked: (dataPoint: DataPoint?) -> Unit,
    onOpenJuiceArticle: (url: String) -> Unit,
    onOpenJuiceSection: () -> Unit,
    onSeeInvestmentDocumentClicked: () -> Unit = {},
    updateCheckedStates: (List<NameValueChecked>, List<NameValueChecked>) -> Unit,
    onActivitiesClicked: (Transaction) -> Unit,
    onOpenDocument: (Document) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 24.dp),
            text = stringResource(R.string.app_title),
            style = FontStyles.TitleSmallRegular,
            textAlign = TextAlign.Center
        )
        TabsSelector(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 12.dp),
            tabs = listOf(
                TabItem(
                    title = stringResource(R.string.text_my_net_worth),
                    content = {
                        MyNetWorthUI(
                            banking = state.bankingAssets,
                            investment = state.investmentAssets,
                            custom = state.customAssets,
                            liabilities = state.liabilities,
                            activities = state.activities,
                            documents = state.documents,
                            updateCheckedStates = updateCheckedStates,
                            onAddAssetClicked = onAddAssetClicked,
                            onAddLiabilityClicked = onAddLiabilityClicked,
                            onOpenJuiceArticle = onOpenJuiceArticle,
                            onOpenJuiceSection = onOpenJuiceSection,
                            onSeeInvestmentDocumentClicked = onSeeInvestmentDocumentClicked,
                            onActivitiesClicked = onActivitiesClicked,
                            onOpenDocument = onOpenDocument
                        )
                    }
                ),
                TabItem(
                    title = stringResource(R.string.text_my_portfolio),
                    content = {
                        MyPortfolioUI(
                            accounts = state.accounts,
                            performance = state.performance,
                            onOpenAccount = onOpenAccount
                        )
                    }
                )
            )
        )
    }
}