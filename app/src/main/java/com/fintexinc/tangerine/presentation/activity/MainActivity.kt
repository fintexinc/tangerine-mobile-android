package com.fintexinc.tangerine.presentation.activity

import android.content.Intent
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.fintexinc.core.R
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.presentation.ui.DashboardScreenUI
import com.fintexinc.dashboard.presentation.ui.screen.AddEditAssetUI
import com.fintexinc.dashboard.presentation.ui.screen.AddEditLiabilityUI
import com.fintexinc.dashboard.presentation.viewmodel.DashboardViewModel
import com.fintexinc.tangerine.presentation.ui.SplashScreenUI
import com.tangerine.account.presentation.ui.AccountScreen
import com.tangerine.account.presentation.viewmodel.AccountViewModel
import com.tangerine.documents.presentation.ui.ui.AccountDocumentsUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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

                LaunchedEffect(true) {
                    dashboardViewModel.action.collectLatest { action ->
                        when (action) {
                            is DashboardViewModel.Action.AddEditAsset -> {
                                navController.navigate(Routes.AddEditAsset(action.assetId))
                            }

                            is DashboardViewModel.Action.AddEditLiability -> {
                                navController.navigate(Routes.AddEditLiability(action.liabilityId))
                            }
                        }
                    }
                }

                NavHost(navController, startDestination = Routes.Splash) {

                    composable<Routes.Splash> {
                        SplashScreenUI(
                            onSplashComplete = {
                                shouldShowSplashScreen.value = false
                                navController.navigate(Routes.MainRoute)
                            }
                        )
                    }

                    composable<Routes.Account> {
                        val args = it.toRoute<Routes.Account>()
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
                                navController.navigate(Routes.Documents)
                            },
                            onTabSelected = { tab ->
                                accountViewModel.onTabChanged(
                                    tab = tab,
                                    accountId = args.accountId
                                )
                            }
                        )
                    }
                    composable<Routes.MainRoute> {
                        MainRoute(
                            parentNavController = navController,
                            dashboardViewModel = dashboardViewModel,
                        )
                    }
                    composable<Routes.AddEditAsset> {
                        val args = it.toRoute<Routes.AddEditAsset>()
                        AddEditAssetUI(
                            asset = if (args.assetId != null) {
                                dashboardViewModel.getCustomAssetById(args.assetId)
                            } else null,
                            onSaveAssetClick = { asset ->
                                dashboardViewModel.onAddAsset(asset)
                                navController.popBackStack()
                            },
                            onDeleteAsset = { asset ->
                                dashboardViewModel.onDeleteAsset(asset)
                                navController.popBackStack()
                            },
                            onBackButtonFromExternalScreenClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable<Routes.AddEditLiability> {
                        val args = it.toRoute<Routes.AddEditLiability>()
                        AddEditLiabilityUI(
                            liability = if (args.liabilityId != null) {
                                dashboardViewModel.getLiabilityById(args.liabilityId)
                            } else null,
                            onSaveLiabilityClick = { liability ->
                                dashboardViewModel.onAddLiability(liability)
                                navController.popBackStack()
                            },
                            onDeleteLiability = { liability ->
                                dashboardViewModel.onDeleteLiability(liability)
                                navController.popBackStack()
                            },
                            onBackButtonFromExternalScreenClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun MainRoute(
        parentNavController: NavHostController,
        dashboardViewModel: DashboardViewModel
    ) {
        val tabNavController = rememberNavController()

        val bottomMenuSelectedItem = remember {
            mutableStateOf<Routes.MenuItem>(
                Routes.MenuItem.Wealth
            )
        }

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
                        selected = bottomMenuSelectedItem == Routes.MenuItem.Accounts,
                        onClick = {
                            bottomMenuSelectedItem.value = Routes.MenuItem.Accounts
                            //navController.navigate(Account)
                        },
                        label = {
                            Text(
                                modifier = Modifier.wrapContentSize(),
                                text = "Accounts",
                                style = if (bottomMenuSelectedItem.value == Routes.MenuItem.Accounts) FontStyles.BodySmallBold else FontStyles.BodySmall
                            )
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.ic_menu_accounts),
                                tint = if (bottomMenuSelectedItem.value == Routes.MenuItem.Accounts) Colors.BackgroundPrimary else Colors.BrandBlack,
                                contentDescription = "Accounts Icon"
                            )
                        }
                    )
                    NavigationBarItem(
                        modifier = Modifier
                            .padding(top = 12.dp),
                        selected = bottomMenuSelectedItem == Routes.MenuItem.Wealth,
                        onClick = {
                            bottomMenuSelectedItem.value = Routes.MenuItem.Wealth
                            tabNavController.navigate(Routes.Dashboard)
                        },
                        label = {
                            Text(
                                modifier = Modifier.wrapContentSize(),
                                text = "Wealth",
                                style = if (bottomMenuSelectedItem.value == Routes.MenuItem.Wealth) FontStyles.BodySmallBold else FontStyles.BodySmall
                            )
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter =
                                    if (bottomMenuSelectedItem.value == Routes.MenuItem.Wealth) {
                                        painterResource(R.drawable.ic_menu_wealth_selected)
                                    } else {
                                        painterResource(R.drawable.ic_menu_wealth_unselected)
                                    },
                                tint = Color.Unspecified,
                                contentDescription = stringResource(com.fintexinc.tangerine.R.string.description_icon_wealth)
                            )
                        }
                    )
                    NavigationBarItem(
                        modifier = Modifier.padding(top = 12.dp),
                        selected = bottomMenuSelectedItem == Routes.MenuItem.Tracker,
                        onClick = {
                            bottomMenuSelectedItem.value = Routes.MenuItem.Tracker
                        },
                        label = {
                            Text(
                                modifier = Modifier.wrapContentSize(),
                                text = "Tracker",
                                style = if (bottomMenuSelectedItem.value == Routes.MenuItem.Tracker) FontStyles.BodySmallBold else FontStyles.BodySmall
                            )
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.ic_menu_tracker),
                                tint = if (bottomMenuSelectedItem.value == Routes.MenuItem.Tracker) Colors.BackgroundPrimary else Colors.BrandBlack,
                                contentDescription = "Accounts Icon"
                            )
                        }
                    )
                    NavigationBarItem(
                        modifier = Modifier.padding(top = 12.dp),
                        selected = bottomMenuSelectedItem == Routes.MenuItem.MoveMoney,
                        onClick = {
                            bottomMenuSelectedItem.value = Routes.MenuItem.MoveMoney
                        },
                        label = {
                            Text(
                                modifier = Modifier.wrapContentSize(),
                                text = "Money",
                                style = if (bottomMenuSelectedItem.value == Routes.MenuItem.MoveMoney) FontStyles.BodySmallBold else FontStyles.BodySmall
                            )
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.ic_menu_make_money),
                                tint = if (bottomMenuSelectedItem.value == Routes.MenuItem.MoveMoney) Colors.BackgroundPrimary else Colors.BrandBlack,
                                contentDescription = "Accounts Icon"
                            )
                        }
                    )
                    NavigationBarItem(
                        modifier = Modifier.padding(top = 12.dp),
                        selected = bottomMenuSelectedItem == Routes.MenuItem.More,
                        onClick = {
                            bottomMenuSelectedItem.value = Routes.MenuItem.More
                        },
                        label = {
                            Text(
                                modifier = Modifier.wrapContentSize(),
                                text = "More",
                                style = if (bottomMenuSelectedItem.value == Routes.MenuItem.More) FontStyles.BodySmallBold else FontStyles.BodySmall
                            )
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.ic_menu_more),
                                tint = if (bottomMenuSelectedItem.value == Routes.MenuItem.More) Colors.BackgroundPrimary else Colors.BrandBlack,
                                contentDescription = "Accounts Icon"
                            )
                        }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = tabNavController,
                startDestination = Routes.Dashboard,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<Routes.Dashboard> {
                    val state = dashboardViewModel.state.collectAsState()
                    DashboardScreenUI(
                        state.value,
                        onPlatformClicked = {
                            dashboardViewModel.onPlatformClicked()
                        },
                        onOpenAccountClicked = {
                            parentNavController.navigate(Routes.Account(it))
                        },
                        onAddAssetClicked = { dataPoint ->
                            dashboardViewModel.onAddAssetClicked(dataPoint)
                        },
                        onAddLiabilityClicked = { dataPoint ->
                            dashboardViewModel.onAddLiabilityClicked(dataPoint)
                        },
                        onOpenJuiceArticle = { url ->
                            goToJuiceArticle(url)
                        },
                        onOpenJuiceSection = {
                            goToJuiceSection()
                        },
                        onOpenDocumentsClicked = {
                            tabNavController.navigate(Routes.Documents)
                        },
                        updateCheckedStates = { assets, liabilities ->
                            dashboardViewModel.updateCheckedStates(
                                assets,
                                liabilities,
                            )
                        }
                    )
                }

                composable<Routes.Documents> {
                    AccountDocumentsUI(
                        onBackClicked = {
                            parentNavController.popBackStack()
                        }
                    )
                }
            }
        }
    }

    private fun goToJuiceArticle(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        this.startActivity(intent)
    }

    private fun goToJuiceSection() {
        val intent = Intent(Intent.ACTION_VIEW, "https://www.tangerine.ca/en/thejuice".toUri())
        this.startActivity(intent)
    }
}

object Routes {

    @Serializable
    object Dashboard

    @Serializable
    data class Account(val accountId: String)

    @Serializable
    object Documents

    @Serializable
    object Splash

    @Serializable
    object MainRoute

    @Serializable
    data class AddEditAsset(val assetId: String?)

    @Serializable
    data class AddEditLiability(val liabilityId: String?)

    sealed class MenuItem {
        object Accounts : MenuItem()
        object Wealth : MenuItem()
        object Tracker : MenuItem()
        object MoveMoney : MenuItem()
        object More : MenuItem()
    }
}