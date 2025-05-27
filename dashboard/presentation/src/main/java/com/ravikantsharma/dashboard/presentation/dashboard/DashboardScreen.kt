package com.ravikantsharma.dashboard.presentation.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravikantsharma.core.domain.formatting.NumberFormatter
import com.ravikantsharma.core.domain.model.Currency
import com.ravikantsharma.core.domain.model.DecimalSeparator
import com.ravikantsharma.core.domain.model.ExpenseFormat
import com.ravikantsharma.core.domain.model.ThousandsSeparator
import com.ravikantsharma.core.presentation.designsystem.DownloadButton
import com.ravikantsharma.core.presentation.designsystem.ExpenseManagerTheme
import com.ravikantsharma.core.presentation.designsystem.LocalStatusBarAppearance
import com.ravikantsharma.core.presentation.designsystem.R.drawable
import com.ravikantsharma.core.presentation.designsystem.SettingsButton
import com.ravikantsharma.core.presentation.designsystem.StatusBarAppearance
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerScaffold
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerTopBar
import com.ravikantsharma.core.presentation.designsystem.components.LargestTransactionView
import com.ravikantsharma.core.presentation.designsystem.components.PopularCategoryView
import com.ravikantsharma.core.presentation.designsystem.components.PreviousWeekTotalView
import com.ravikantsharma.core.presentation.designsystem.components.TransactionItemView
import com.ravikantsharma.core.presentation.designsystem.components.buttons.ExManagerFloatingActionButton
import com.ravikantsharma.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.ravikantsharma.dashboard.core.R
import com.ravikantsharma.dashboard.presentation.create_screen.CreateTransactionScreenRoot
import com.ravikantsharma.dashboard.presentation.export.ExportTransactionsScreenRoot
import com.ravikantsharma.ui.LocalAuthActionHandler
import com.ravikantsharma.ui.ObserveAsEvents
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToAllTransactions: () -> Unit
) {
    val authActionHandler = LocalAuthActionHandler.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val createBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val exportBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    EventHandler(viewModel, onNavigateToAllTransactions, onNavigateToSettings)

    CompositionLocalProvider(
        LocalStatusBarAppearance provides StatusBarAppearance(isDarkStatusBarIcons = false)
    ) {
        DashboardScreen(
            modifier = modifier,
            uiState = uiState,
            createBottomSheetState = createBottomSheetState,
            exportBottomSheetState = exportBottomSheetState,
            onAction = { action ->
                onActionHandler(action, authActionHandler, viewModel)
            }
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun onActionHandler(
    action: DashboardAction,
    authActionHandler: ((() -> Unit) -> Unit)?,
    viewModel: DashboardViewModel
) {
    when (action) {
        is DashboardAction.UpdatedBottomSheet -> {
            if (action.showSheet) {
                authActionHandler?.invoke {
                    viewModel.onAction(action)
                }
            } else {
                viewModel.onAction(action)
            }
        }

        is DashboardAction.UpdateExportBottomSheet -> {
            if (action.showSheet) {
                authActionHandler?.invoke {
                    viewModel.onAction(action)
                }
            } else {
                viewModel.onAction(action)
            }
        }

        else -> viewModel.onAction(action)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun EventHandler(
    viewModel: DashboardViewModel,
    onNavigateToAllTransactions: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    ObserveAsEvents(viewModel.events) {
        when (it) {
            DashboardEvent.NavigateToAllTransactions -> onNavigateToAllTransactions()
            DashboardEvent.NavigateToSettings -> onNavigateToSettings()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    uiState: DashboardViewState,
    createBottomSheetState: SheetState,
    exportBottomSheetState: SheetState,
    onAction: (DashboardAction) -> Unit,
) {
    DashboardBottomSheets(uiState, onAction, createBottomSheetState, exportBottomSheetState)

    ExManagerScaffold(
        withGradient = true,
        topBar = { DashboardTopBar(uiState, onAction) },
        floatingActionButton = {
            ExManagerFloatingActionButton(
                onClick = {
                    onAction(DashboardAction.UpdatedBottomSheet(true))
                }
            )
        },
    ) { contentPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            Column {
                DashboardTransactionsContent(uiState)

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        if (uiState.transactions.isNullOrEmpty()) {
                            EmptyTransactionView()
                        } else {
                            LatestTransactionsView(uiState, onAction)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardTransactionsContent(uiState: DashboardViewState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = uiState.accountBalance,
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
        Text(
            modifier = Modifier
                .padding(top = 2.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.account_balance),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.80f
                )
            )
        )
        Spacer(modifier = Modifier.height(46.dp))
        uiState.mostPopularCategory?.let { popularCategory ->
            PopularCategoryView(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                icon = popularCategory.symbol,
                title = popularCategory.title,
                description = stringResource(R.string.most_popular_category)
            )
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .height(IntrinsicSize.Max)
        ) {
            LargestTransactionView(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isEmptyTransactions = uiState.largestTransaction == null,
                title = uiState.largestTransaction?.name.orEmpty(),
                description = stringResource(R.string.largest_transaction),
                amount = uiState.largestTransaction?.amount ?: "",
                date = uiState.largestTransaction?.date.orEmpty()
            )

            Spacer(modifier = Modifier.width(8.dp))

            PreviousWeekTotalView(
                modifier = Modifier.fillMaxHeight(),
                amount = uiState.previousWeekTotal,
                description = stringResource(R.string.previous_week)
            )
        }
    }
}

@Composable
private fun DashboardTopBar(
    uiState: DashboardViewState,
    onAction: (DashboardAction) -> Unit
) {
    ExManagerTopBar(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        startIcon = null,
        endIcon1 = DownloadButton,
        onEndIcon1Click = {
            onAction(DashboardAction.UpdateExportBottomSheet(true))
        },
        endIcon2 = SettingsButton,
        onEndIcon2Click = {
            onAction(DashboardAction.OnSettingsClicked)
        },
        endIcon1Color = MaterialTheme.colorScheme.onPrimary,
        endIcon1BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
        endIcon2Color = MaterialTheme.colorScheme.onPrimary,
        endIcon2BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
        title = uiState.username,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DashboardBottomSheets(
    uiState: DashboardViewState,
    onAction: (DashboardAction) -> Unit,
    createBottomSheetState: SheetState,
    exportBottomSheetState: SheetState
) {
    if (uiState.showCreateTransactionSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onAction(DashboardAction.UpdatedBottomSheet(false))
            },
            sheetState = createBottomSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false
            ),
            dragHandle = null,
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(
                    WindowInsets.statusBars.union(WindowInsets.displayCutout.only(WindowInsetsSides.Top))
                )
        ) {
            CreateTransactionScreenRoot(
                onDismiss = {
                    onAction(DashboardAction.UpdatedBottomSheet(false))
                }
            )
        }
    }

    if (uiState.showExportTransactionSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onAction(DashboardAction.UpdateExportBottomSheet(false))
            },
            sheetState = exportBottomSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false
            ),
            dragHandle = null,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
            ) {
                ExportTransactionsScreenRoot(
                    onDismiss = {
                        onAction(DashboardAction.UpdateExportBottomSheet(false))
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyTransactionView() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(96.dp),
            painter = painterResource(drawable.ic_money),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(R.string.no_transactions_to_show),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LatestTransactionsView(
    uiState: DashboardViewState,
    onAction: (DashboardAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.latest_transactions),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier.clickable {
                onAction(DashboardAction.OnShowAllTransactionsClicked)
            },
            text = stringResource(R.string.show_all),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        uiState.transactions?.forEach { transactionGroup ->
            stickyHeader {
                Text(
                    text = transactionGroup.dateLabel,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.70f
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 4.dp)
                )
            }

            items(
                items = transactionGroup.transactions,
                key = { transaction -> transaction.transactionId }
            ) { transaction ->
                TransactionItemView(
                    icon = transaction.transactionCategory.symbol,
                    title = transaction.title,
                    category = transaction.transactionCategory.title,
                    amount = transaction.amount,
                    note = transaction.note,
                    displayAmount = { amount ->
                        NumberFormatter.formatAmount(
                            amount = amount,
                            expenseFormat = uiState.preference?.expenseFormat
                                ?: ExpenseFormat.MINUS_PREFIX,
                            decimalSeparator = uiState.preference?.decimalSeparator
                                ?: DecimalSeparator.DOT,
                            thousandsSeparator = uiState.preference?.thousandsSeparator
                                ?: ThousandsSeparator.COMMA,
                            currency = uiState.preference?.currency
                                ?: Currency.USD
                        )
                    },
                    isCollapsed = transaction.isCollapsed,
                    onCardClicked = {
                        onAction(
                            DashboardAction.OnCardClicked(
                                transactionId = transaction.transactionId
                            )
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun PreviewDashboardScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DashboardScreen(
                modifier = Modifier,
                uiState = DashboardViewState(
                    username = "Hrishi",
                    accountBalance = "\$10,382.45",
                    mostPopularCategory = TransactionCategoryTypeUI.TRANSPORTATION,
                    previousWeekTotal = "\$100.45",
                    largestTransaction = LargestTransaction(
                        name = "Adobe Yearly",
                        amount = "-\$59.99",
                        date = "Jan 7, 2025"
                    ),
                    transactions = listOf(
                        TransactionGroupUIItem(
                            dateLabel = "TODAY",
                            transactions = listOf(
                                TransactionUIItem(
                                    transactionId = 1,
                                    transactionCategory = TransactionCategoryTypeUI.OTHER,
                                    title = "Amazon",
                                    note = "Hi",
                                    amount = BigDecimal.TEN.negate(),
                                    date = LocalDateTime.of(
                                        2025,
                                        Month.MARCH,
                                        30,
                                        0,
                                        0
                                    )
                                )
                            )
                        ),
                        TransactionGroupUIItem(
                            dateLabel = "JANUARY 9",
                            transactions = listOf(
                                TransactionUIItem(
                                    transactionId = 2,
                                    transactionCategory = TransactionCategoryTypeUI.OTHER,
                                    title = "Amazon",
                                    note = "Hi",
                                    amount = BigDecimal.TEN.negate(),
                                    date = LocalDateTime.of(
                                        2025,
                                        Month.MARCH,
                                        30,
                                        0,
                                        0
                                    )
                                )
                            )
                        )
                    )
                ),
                onAction = {

                },
                createBottomSheetState = rememberModalBottomSheetState(),
                exportBottomSheetState = rememberModalBottomSheetState(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun PreviewDashboardEmptyScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DashboardScreen(
                modifier = Modifier,
                uiState = DashboardViewState(
                    username = "Hrishi",
                    accountBalance = "\$10,382.45",
                    mostPopularCategory = TransactionCategoryTypeUI.TRANSPORTATION,
                    previousWeekTotal = "\$100.45",
                    largestTransaction = LargestTransaction(
                        name = "Adobe Yearly",
                        amount = "-\$59.99",
                        date = "Jan 7, 2025"
                    ),
                    transactions = listOf()
                ),
                onAction = {

                },
                createBottomSheetState = rememberModalBottomSheetState(),
                exportBottomSheetState = rememberModalBottomSheetState(),
            )
        }
    }
}
