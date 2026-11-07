
package ua.edu.mobile.smartlife.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ua.edu.mobile.smartlife.data.AppContainer
import ua.edu.mobile.smartlife.ui.auth.LoginScreen
import ua.edu.mobile.smartlife.ui.components.AddRecordDialog
import ua.edu.mobile.smartlife.ui.home.HomeScreen
import ua.edu.mobile.smartlife.ui.profile.ProfileScreen
import ua.edu.mobile.smartlife.ui.records.RecordDetailsScreen
import ua.edu.mobile.smartlife.ui.records.RecordsScreen
import ua.edu.mobile.smartlife.ui.records.RecordsViewModel
import ua.edu.mobile.smartlife.ui.records.RecordsViewModelFactory
import ua.edu.mobile.smartlife.ui.statistics.StatisticsScreen

@Composable
fun SmartLifeApp() {

    val navController = rememberNavController()

    val appContainer = remember { AppContainer() }

    val recordsViewModel: RecordsViewModel = viewModel(
        factory = RecordsViewModelFactory(appContainer.recordRepository)
    )

    val records by recordsViewModel.records.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    val selectedType by recordsViewModel.selectedType.collectAsStateWithLifecycle()

    var showAddRecordDialog by remember {
        mutableStateOf(false)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val homeRoute = HomeRoute::class.qualifiedName
    val recordsRoute = RecordsRoute::class.qualifiedName
    val profileRoute = ProfileRoute::class.qualifiedName
    val statisticsRoute = StatisticsRoute::class.qualifiedName

    val showBottomBar =
        currentRoute == homeRoute ||
                currentRoute == recordsRoute ||
                currentRoute == profileRoute ||
                currentRoute == statisticsRoute

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {

                    NavigationBarItem(
                        selected = currentRoute == homeRoute,
                        onClick = {
                            navController.navigate(HomeRoute) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Головна"
                            )
                        },
                        label = { Text("Головна") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == recordsRoute,
                        onClick = {
                            navController.navigate(RecordsRoute) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Журнал"
                            )
                        },
                        label = { Text("Журнал") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == statisticsRoute,
                        onClick = {
                            navController.navigate(StatisticsRoute) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Статистика"
                            )
                        },
                        label = { Text("Статистика") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == profileRoute,
                        onClick = {
                            navController.navigate(ProfileRoute) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Профіль"
                            )
                        },
                        label = { Text("Профіль") }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = LoginRoute,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable<LoginRoute> {
                LoginScreen(
                    onLogin = {
                        navController.navigate(HomeRoute) {
                            popUpTo<LoginRoute> {
                                inclusive = true
                            }
                        }
                    },
                    onRegister = {
                        navController.navigate(RegisterRoute)
                    }
                )
            }

            composable<HomeRoute> {
                HomeScreen(
                    records = records,
                    onRecordClick = { recordId ->
                        navController.navigate(RecordDetailsRoute(recordId))
                    },
                    onAddRecord = {
                        showAddRecordDialog = true
                    }
                )
            }

            composable<RecordsRoute> {
                RecordsScreen(
                    records = records,
                    selectedType = selectedType,
                    onFilterChange = { type ->
                        recordsViewModel.setFilter(type)
                    },
                    onRecordClick = { recordId ->
                        navController.navigate(RecordDetailsRoute(recordId))
                    },
                    onAddRecord = {
                        showAddRecordDialog = true
                    },
                    onDeleteRecord = { recordId ->
                        recordsViewModel.deleteRecord(recordId)
                    },
                    onUndoDelete = {
                        recordsViewModel.undoDelete()
                    }
                )
            }

            composable<RecordDetailsRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<RecordDetailsRoute>()

                val record = records.find {
                    it.id == route.recordId
                }

                if (record != null) {
                    RecordDetailsScreen(
                        record = record,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable<ProfileRoute> {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(LoginRoute) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<StatisticsRoute> {
                StatisticsScreen(
                    records = records
                )
            }
        }

        if (showAddRecordDialog) {
            AddRecordDialog(
                onDismiss = {
                    showAddRecordDialog = false
                },
                onSave = { type, value, note ->
                    recordsViewModel.addRecord(
                        type = type,
                        value = value,
                        note = note
                    )
                    showAddRecordDialog = false
                }
            )
        }
    }
}
