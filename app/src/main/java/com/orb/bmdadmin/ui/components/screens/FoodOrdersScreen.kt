package com.orb.bmdadmin.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.orb.bmdadmin.data.OrderListViewModel
import com.orb.bmdadmin.data.ReservedFood
import com.orb.bmdadmin.data.SnackBarMessage
import com.orb.bmdadmin.ui.components.FoodOrderItem
import com.orb.bmdadmin.ui.components.MySnackBar
import com.orb.bmdadmin.ui.theme.AppTheme
import com.orb.bmdadmin.ui.theme.SurfaceVariant

@Composable
fun FoodOrdersScreen(
    modifier: Modifier = Modifier,
    orderListViewModel: OrderListViewModel = hiltViewModel(),
    onFoodClicked: (ReservedFood, String) -> Unit
) {
    val screenState by orderListViewModel.orderListScreenState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf("All") }
    var selectedRestoreItem by remember { mutableStateOf<ReservedFood?>(null) }

    LaunchedEffect(screenState.snackBarMessage) {
        screenState.snackBarMessage?.let { message ->
            when (message) {
                is SnackBarMessage.Undoable -> {
                    val result = snackBarHostState.showSnackbar(
                        message = message.message,
                        actionLabel = message.actionLabel,
                        duration = SnackbarDuration.Short
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        orderListViewModel.undoMoveToReceived(message.documentId, message.lastStatus)
                    }
                }

                is SnackBarMessage.Success -> {
                    snackBarHostState.showSnackbar(message = message.message)
                }

                is SnackBarMessage.Error -> {
                    snackBarHostState.showSnackbar(message.message)
                }
            }
            orderListViewModel.clearSnackBar()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = modifier
                    .navigationBarsPadding(),
                snackbar = { data ->
                    MySnackBar(snackBarData = data)
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .systemBarsPadding()
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    SelectableChip(
                        text = "All",
                        isSelected = selectedTab == "All",
                        onClick = {
                            selectedTab = "All"
                        }
                    )
                    SelectableChip(
                        text = "Order",
                        isSelected = selectedTab == "Order",
                        onClick = {
                            selectedTab = "Order"
                        }
                    )
                    SelectableChip(
                        text = "Reserved",
                        isSelected = selectedTab == "Reserved",
                        onClick = {
                            selectedTab = "Reserved"
                        }
                    )
                    SelectableChip(
                        text = "Received",
                        isSelected = selectedTab == "Received",
                        onClick = {
                            selectedTab = "Received"
                        }
                    )
                }

                when (selectedTab) {
                    "All" -> AllList(
                        items = screenState.allFoods,
                        onFoodClicked = onFoodClicked,
                        onItemSwiped = { index, item ->
//                            orderListViewModel.deleteItem(index)
                            orderListViewModel.moveToReceived(item.documentId, status = item.status)
                        }
                    )

                    "Order" -> OrderList(
                        items = screenState.orderList,
                        onFoodClicked = onFoodClicked,
                        onItemSwiped = { index, item ->
                            orderListViewModel.moveToReceived(item.documentId, status = item.status)
                        }
                    )

                    "Reserved" -> ReservedList(
                        items = screenState.reserveList,
                        onFoodClicked = onFoodClicked,
                        onItemSwiped = { index, item ->
                            orderListViewModel.moveToReceived(item.documentId, status = item.status)
                        }
                    )

                    "Received" -> ReceivedList(
                        items = screenState.receivedList,
                        onFoodClicked = onFoodClicked,
                        restoreIndex = {},
                        onRestoreClicked = { item ->
                            selectedRestoreItem = item
                        }
                    )
                }
            }

            // Restore Button
            if (selectedTab == "Received" && selectedRestoreItem != null) {
                Popup(
                    alignment = Alignment.BottomCenter,
                    onDismissRequest = { selectedRestoreItem = null }
                ) {
                    Button(
                        onClick = {
                            selectedRestoreItem?.let { item ->
//                                orderListViewModel.restoreItemFromReceived(item)
                                orderListViewModel.undoMoveToReceived(item.documentId, lastStatus = item.lastStatus)
                                selectedRestoreItem = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onBackground,
                            contentColor = MaterialTheme.colorScheme.background
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Text("Restore")
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectableChip(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.onBackground
        else SurfaceVariant,
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            modifier = modifier
                .padding(horizontal = 12.dp)
                .padding(vertical = 6.dp),
            color = if (isSelected) MaterialTheme.colorScheme.background else
                MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun AllList(
    modifier: Modifier = Modifier,
    items: List<ReservedFood>,
    onFoodClicked: (ReservedFood, String) -> Unit,
    onItemSwiped: (Int, ReservedFood) -> Unit
) {
    SwipableList(
        items = items,
        onFoodClicked = onFoodClicked,
        onItemSwiped = onItemSwiped
    )
}

@Composable
fun OrderList(
    modifier: Modifier = Modifier,
    items: List<ReservedFood>,
    onFoodClicked: (ReservedFood, String) -> Unit,
    onItemSwiped: (Int, ReservedFood) -> Unit
) {
    SwipableList(
        items = items,
        onFoodClicked = onFoodClicked,
        onItemSwiped = onItemSwiped
    )
}

@Composable
fun ReservedList(
    modifier: Modifier = Modifier,
    items: List<ReservedFood>,
    onFoodClicked: (ReservedFood, String) -> Unit,
    onItemSwiped: (Int, ReservedFood) -> Unit
) {
    SwipableList(
        items = items,
        onFoodClicked = onFoodClicked,
        onItemSwiped = onItemSwiped
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipableList(
    modifier: Modifier = Modifier,
    items: List<ReservedFood>,
    onFoodClicked: (ReservedFood, String) -> Unit,
    onItemSwiped: (Int, ReservedFood) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(items) { index, item ->
            val dismissState = rememberSwipeToDismissBoxState()

            if (dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd ||
                dismissState.currentValue == SwipeToDismissBoxValue.EndToStart
            ) {
                LaunchedEffect(Unit) {
                    onItemSwiped(index, item)
                    dismissState.reset()
                }
            }

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Green)
                    )
                }
            ) {
                Box(
                    modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    FoodOrderItem(
                        food = item,
                        onFoodClicked = {
                            onFoodClicked(item, item.imgUrl)
                        },
                        onFoodHeld = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun ReceivedList(
    modifier: Modifier = Modifier,
    items: List<ReservedFood>,
    onFoodClicked: (ReservedFood, String) -> Unit,
    restoreIndex: (Int) -> Unit,
    onRestoreClicked: (ReservedFood) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(items) { index, item ->
            restoreIndex(index)
            Box(
                modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                FoodOrderItem(
                    food = item,
                    onFoodClicked = {
                        onFoodClicked(item, item.imgUrl)
                    },
                    onFoodHeld = {
                        onRestoreClicked(item)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PrevFoodOrdersScreen() {
    AppTheme {
        FoodOrdersScreen { f, s ->

        }
    }
}