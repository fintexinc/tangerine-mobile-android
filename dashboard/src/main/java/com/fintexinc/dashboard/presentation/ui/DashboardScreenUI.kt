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
import com.fintexinc.core.domain.model.Custom
import com.fintexinc.core.data.model.DataPoint
import com.fintexinc.core.domain.model.Liability
import com.fintexinc.core.presentation.ui.widget.TabItem
import com.fintexinc.core.presentation.ui.widget.TabsSelector
import com.fintexinc.core.presentation.ui.widget.modal.NameValueChecked
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R
import com.fintexinc.dashboard.presentation.ui.screen.AddEditAssetUI
import com.fintexinc.dashboard.presentation.ui.screen.AddEditLiabilityUI
import com.fintexinc.dashboard.presentation.ui.screen.MyNetWorthUI
import com.fintexinc.dashboard.presentation.ui.screen.MyPortfolioUI
import com.fintexinc.dashboard.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreenUI(
    state: DashboardViewModel.State,
    onPlatformClicked: () -> Unit,
    onOpenAccountClicked: (accountId: String) -> Unit,
    onAddAssetClicked : (DataPoint?) -> Unit,
    onAddAsset: (Custom) -> Unit,
    onDeleteAsset: (Custom) -> Unit,
    onAddLiabilityClicked: (DataPoint?) -> Unit,
    onAddLiability: (Liability) -> Unit,
    onOpenJuiceArticle: (url: String) -> Unit,
    onDeleteLiability: (Liability) -> Unit,
    onBackButtonFromExternalScreenClicked: () -> Unit,
    updateCheckedStates: (List<NameValueChecked>, List<NameValueChecked>) -> Unit
) {
    when (state) {
        is DashboardViewModel.State.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DashboardViewModel.State.Data -> Content(
            state = state,
            onPlatformClicked = onPlatformClicked,
            onOpenAccount = onOpenAccountClicked,
            onAddAssetClicked = onAddAssetClicked,
            onAddLiabilityClicked = onAddLiabilityClicked,
            onOpenJuiceArticle = onOpenJuiceArticle,
            updateCheckedStates
        )

        is DashboardViewModel.State.AddEditAsset -> {
            AddEditAssetUI(
                asset = state.asset,
                onSaveAssetClick = onAddAsset,
                onDeleteAsset = onDeleteAsset,
                onBackButtonFromExternalScreenClicked = onBackButtonFromExternalScreenClicked
            )
        }
        is DashboardViewModel.State.AddEditLiability -> {
            AddEditLiabilityUI(
                liability = state.liability,
                onSaveLiabilityClick = onAddLiability,
                onDeleteLiability = onDeleteLiability,
                onBackButtonFromExternalScreenClicked = onBackButtonFromExternalScreenClicked
            )
        }
    }
}

@Composable
private fun Content(
    state: DashboardViewModel.State.Data,
    onPlatformClicked: () -> Unit,
    onOpenAccount:(accountId: String) -> Unit,
    onAddAssetClicked : (dataPoint: DataPoint?) -> Unit,
    onAddLiabilityClicked: (dataPoint: DataPoint?) -> Unit,
    onOpenJuiceArticle: (url: String) -> Unit,
    updateCheckedStates: (List<NameValueChecked>, List<NameValueChecked>) -> Unit
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
        TabsSelector(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 18.dp, top = 12.dp, end = 18.dp),
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
                            onOpenJuiceArticle = onOpenJuiceArticle
                        )
                    }
                ),
                TabItem(
                    title = stringResource(R.string.text_my_portfolio),
                    content = {
                        MyPortfolioUI(
                            accounts = state.accounts,
                            onOpenAccount = onOpenAccount
                        )
                    }
                )
            )
        )
    }
}