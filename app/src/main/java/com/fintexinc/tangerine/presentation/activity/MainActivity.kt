package com.fintexinc.tangerine.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.fintexinc.core.R
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.presentation.ui.DashboardScreenUI
import com.fintexinc.dashboard.presentation.viewmodel.DashboardViewModel
import com.fintexinc.tangerine.presentation.ui.SplashScreenUI
import com.tangerine.account.presentation.ui.AccountScreen
import com.tangerine.account.presentation.viewmodel.AccountViewModel
import com.tangerine.documents.presentation.ui.AccountDocumentsUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val accountViewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val shouldShowSplashScreen = remember {
                    mutableStateOf(true)
                }
                val bottomMenuSelectedItem = remember {
                    mutableStateOf<MenuItem>(
                        MenuItem.Wealth
                    )
                }
                if (shouldShowSplashScreen.value) {
                    SplashScreenUI(
                        onSplashComplete = {
                            shouldShowSplashScreen.value = false
                        }
                    )
                } else {
                    Scaffold(
                        bottomBar = {
                            NavigationBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .shadow(16.dp)
                                    .border(width = 1.dp, color = Colors.BorderSubdued),
                                containerColor = Colors.Background, tonalElevation = 12.dp
                            ) {
                                NavigationBarItem(
                                    modifier = Modifier.padding(top = 12.dp),
                                    selected = bottomMenuSelectedItem == MenuItem.Accounts,
                                    onClick = {
                                        bottomMenuSelectedItem.value = MenuItem.Accounts
                                        //navController.navigate(Account)
                                    },
                                    label = {
                                        Text(
                                            modifier = Modifier.wrapContentSize(),
                                            text = "Accounts",
                                            style = if (bottomMenuSelectedItem.value == MenuItem.Accounts) FontStyles.BodySmallBold else FontStyles.BodySmall
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(R.drawable.ic_menu_accounts),
                                            tint = if (bottomMenuSelectedItem.value == MenuItem.Accounts) Colors.BackgroundPrimary else Colors.BrandBlack,
                                            contentDescription = "Accounts Icon"
                                        )
                                    }
                                )
                                NavigationBarItem(
                                    modifier = Modifier
                                        .padding(top = 12.dp),
                                    selected = bottomMenuSelectedItem == MenuItem.Wealth,
                                    onClick = {
                                        bottomMenuSelectedItem.value = MenuItem.Wealth
                                        navController.navigate(Dashboard)
                                    },
                                    label = {
                                        Text(
                                            modifier = Modifier.wrapContentSize(),
                                            text = "Wealth",
                                            style = if (bottomMenuSelectedItem.value == MenuItem.Wealth) FontStyles.BodySmallBold else FontStyles.BodySmall
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(R.drawable.ic_menu_wealth),
                                            tint = if (bottomMenuSelectedItem.value == MenuItem.Wealth) Colors.BackgroundPrimary else Colors.BrandBlack,
                                            contentDescription = stringResource(com.fintexinc.tangerine.R.string.description_icon_wealth)
                                        )
                                    }
                                )
                                NavigationBarItem(
                                    modifier = Modifier.padding(top = 12.dp),
                                    selected = bottomMenuSelectedItem == MenuItem.Tracker,
                                    onClick = {
                                        bottomMenuSelectedItem.value = MenuItem.Tracker
                                    },
                                    label = {
                                        Text(
                                            modifier = Modifier.wrapContentSize(),
                                            text = "Tracker",
                                            style = if (bottomMenuSelectedItem.value == MenuItem.Tracker) FontStyles.BodySmallBold else FontStyles.BodySmall
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(R.drawable.ic_menu_tracker),
                                            tint = if (bottomMenuSelectedItem.value == MenuItem.Tracker) Colors.BackgroundPrimary else Colors.BrandBlack,
                                            contentDescription = "Accounts Icon"
                                        )
                                    }
                                )
                                NavigationBarItem(
                                    modifier = Modifier.padding(top = 12.dp),
                                    selected = bottomMenuSelectedItem == MenuItem.MoveMoney,
                                    onClick = {
                                        bottomMenuSelectedItem.value = MenuItem.MoveMoney
                                    },
                                    label = {
                                        Text(
                                            modifier = Modifier.wrapContentSize(),
                                            text = "Money",
                                            style = if (bottomMenuSelectedItem.value == MenuItem.MoveMoney) FontStyles.BodySmallBold else FontStyles.BodySmall
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(R.drawable.ic_menu_make_money),
                                            tint = if (bottomMenuSelectedItem.value == MenuItem.MoveMoney) Colors.BackgroundPrimary else Colors.BrandBlack,
                                            contentDescription = "Accounts Icon"
                                        )
                                    }
                                )
                                NavigationBarItem(
                                    modifier = Modifier.padding(top = 12.dp),
                                    selected = bottomMenuSelectedItem == MenuItem.More,
                                    onClick = {
                                        bottomMenuSelectedItem.value = MenuItem.More
                                    },
                                    label = {
                                        Text(
                                            modifier = Modifier.wrapContentSize(),
                                            text = "More",
                                            style = if (bottomMenuSelectedItem.value == MenuItem.More) FontStyles.BodySmallBold else FontStyles.BodySmall
                                        )
                                    },
                                    icon = {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(R.drawable.ic_menu_more),
                                            tint = if (bottomMenuSelectedItem.value == MenuItem.More) Colors.BackgroundPrimary else Colors.BrandBlack,
                                            contentDescription = "Accounts Icon"
                                        )
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = Dashboard,
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable<Dashboard>() {
                                LaunchedEffect(true) {
                                    dashboardViewModel.loadData()
                                }
                                val state = dashboardViewModel.state.collectAsState()
                                DashboardScreenUI(
                                    state.value,
                                    onPlatformClicked = {
                                        dashboardViewModel.onPlatformClicked()
                                    },
                                    onOpenAccountClicked = {
                                        navController.navigate(Account(it))
                                    },
                                    onAddAssetClicked = { dataPoint ->
                                        dashboardViewModel.onAddAssetClicked(dataPoint)
                                    },
                                    onAddAsset = { asset ->
                                        dashboardViewModel.onAddAsset(asset)
                                    },
                                    onDeleteAsset = { asset ->
                                        dashboardViewModel.onDeleteAsset(asset)
                                    },
                                    onAddLiabilityClicked = { dataPoint ->
                                        dashboardViewModel.onAddLiabilityClicked(dataPoint)
                                    },
                                    onAddLiability = { liability ->
                                        dashboardViewModel.onAddLiability(liability)
                                    },
                                    onDeleteLiability = { liability ->
                                        dashboardViewModel.onDeleteLiability(liability)
                                    },
                                    onBackButtonFromExternalScreenClicked = {
                                        dashboardViewModel.loadData()
                                    },
                                    updateCheckedStates = { assets, liabilities ->
                                        dashboardViewModel.updateCheckedStates(
                                            assets,
                                            liabilities,
                                        )
                                    }
                                )
                            }
                            composable<Account>() {
                                val args = it.toRoute<Account>()
                                LaunchedEffect(true) {
                                    accountViewModel.getData(args.accountId)
                                }
                                val state = accountViewModel.state.collectAsState()
                                AccountScreen(
                                    state = state.value,
                                    onBackClicked = {
                                        navController.popBackStack()
                                    },
                                    onOpenDocuments = {
                                        navController.navigate(Documents)
                                    },
                                    onTabSelected = { tab ->
                                        accountViewModel.onTabChanged(
                                            tab = tab,
                                            accountId = args.accountId
                                        )
                                    }
                                )
                            }
                            composable<Documents>() {
                                AccountDocumentsUI(
                                    onBackClicked = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Serializable
    object Dashboard

    @Serializable
    data class Account(val accountId: String)

    @Serializable
    object Documents

    sealed class MenuItem {
        object Accounts : MenuItem()
        object Wealth : MenuItem()
        object Tracker : MenuItem()
        object MoveMoney : MenuItem()
        object More : MenuItem()
    }
}