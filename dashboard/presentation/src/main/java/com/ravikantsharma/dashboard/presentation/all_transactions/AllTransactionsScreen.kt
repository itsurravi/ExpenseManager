package com.ravikantsharma.dashboard.presentation.all_transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.ravikantsharma.core.presentation.designsystem.components.ExManagerTopBar
import com.ravikantsharma.core.presentation.designsystem.components.TransactionItemView
import com.ravikantsharma.core.presentation.designsystem.components.buttons.ExManagerFloatingActionButton
import com.ravikantsharma.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.ravikantsharma.dashboard.presentation.create_screen.CreateTransactionScreenRoot
import com.ravikantsharma.dashboard.presentation.dashboard.TransactionGroupUIItem
import com.ravikantsharma.dashboard.presentation.dashboard.TransactionUIItem
import com.ravikantsharma.dashboard.presentation.export.ExportTransactionsScreenRoot
import com.ravikantsharma.ui.LocalAuthActionHandler
import com.ravikantsharma.ui.ObserveAsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTransactionsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: AllTransactionsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val authActionHandler = LocalAuthActionHandler.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val createBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val exportBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ObserveAsEvent(viewModel.events) {
        when (it) {
            AllTransactionsEvent.NavigateBack -> onNavigateBack()
        }
    }

    AllTransactionsScreen(
        modifier = modifier,
        uiState = uiState,
        createBottomSheetState = createBottomSheetState,
        exportBottomSheetState = exportBottomSheetState,
        scope = scope,
        onAction = { action ->
            when (action) {
                is AllTransactionsAction.UpdateCreateBottomSheet -> {
                    if (action.showSheet) {
                        authActionHandler?.invoke {
                            viewModel.onAction(action)
                        }
                    } else {
                        viewModel.onAction(action)
                    }
                }

                is AllTransactionsAction.UpdateExportBottomSheet -> {
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
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllTransactionsScreen(
    modifier: Modifier = Modifier,
    uiState: AllTransactionsViewState,
    createBottomSheetState: SheetState,
    exportBottomSheetState: SheetState,
    scope: CoroutineScope,
    onAction: (AllTransactionsAction) -> Unit,
) {
    if (uiState.showCreateTransactionsSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onAction(AllTransactionsAction.UpdateCreateBottomSheet(false))
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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CreateTransactionScreenRoot(
                    onDismiss = {
                        onAction(AllTransactionsAction.UpdateCreateBottomSheet(false))
                    }
                )
            }
        }
    }

    if (uiState.showExportTransactionsSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onAction(AllTransactionsAction.UpdateExportBottomSheet(false))
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
                        onAction(AllTransactionsAction.UpdateExportBottomSheet(false))
                    }
                )
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            ExManagerTopBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 8.dp),
                title = "All Transactions",
                titleColor = MaterialTheme.colorScheme.onSurface,
                onStartIconClick = {
                    onAction(AllTransactionsAction.OnClickBackButton)
                },
                endIcon2 = DownloadButton,
                endIcon2Color = MaterialTheme.colorScheme.onSurface,
                endIcon2BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                onEndIcon2Click = {
                    onAction(AllTransactionsAction.UpdateExportBottomSheet(true))
                }
            )
        },
        floatingActionButton = {
            ExManagerFloatingActionButton(
                onClick = {
                    scope.launch {
                        onAction(AllTransactionsAction.UpdateCreateBottomSheet(true))
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
                .padding(horizontal = 16.dp)
        ) {
            LatestTransactionsView(uiState, onAction)
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LatestTransactionsView(
    uiState: AllTransactionsViewState,
    onAction: (AllTransactionsAction) -> Unit
) {
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
                            AllTransactionsAction.OnCardClicked(
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
private fun PreviewAllTransactionsScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AllTransactionsScreen(
                modifier = Modifier,
                uiState = AllTransactionsViewState(
                    transactions = listOf(
                        TransactionGroupUIItem(
                            dateLabel = "TODAY",
                            transactions = listOf(
                                TransactionUIItem(
                                    transactionId = 1,
                                    transactionCategory = TransactionCategoryTypeUI.OTHER,
                                    title = "Amazon",
                                    note = "Hi",
                                    amount = BigDecimal.TEN,
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
                    ),

                    ),
                scope = rememberCoroutineScope(),
                onAction = {

                },
                createBottomSheetState = rememberModalBottomSheetState(),
                exportBottomSheetState = rememberModalBottomSheetState(),
            )
        }
    }
}