package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
private fun SelectableChip(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) null else BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            modifier = modifier
                .padding(horizontal = 12.dp)
                .padding(vertical = 6.dp),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else
                MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteList(
    modifier: Modifier = Modifier
) {
    val activeItems =
        remember { mutableStateListOf("Apple", "Banana", "Cherry", "Date", "Elderberry") }
    val deletedItems = remember { mutableStateListOf<String>() }
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var selectedTab by remember { mutableStateOf("All") }

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                SelectableChip(
                    text = "All",
                    isSelected = selectedTab == "All",
                    onClick = {
                        selectedTab = "All"
                    }
                )
                SelectableChip(
                    text = "Deleted",
                    isSelected = selectedTab == "Deleted",
                    onClick = {
                        selectedTab = "Deleted"
                    }
                )
            }

            if (selectedTab == "All") {
                LazyColumn {
                    item {
                        Text(
                            text = "Active Items",
                            modifier = modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    itemsIndexed(
                        items = activeItems
                    ) { index, item ->
                        val dismissState = rememberSwipeToDismissBoxState()
                        val itemRemoved = remember { mutableStateOf(false) }

                        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart ||
                            dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                            LaunchedEffect(Unit) {
                                if (!itemRemoved.value) {
                                    val removedItem = activeItems.removeAt(index)
                                    deletedItems.add(removedItem)
                                    itemRemoved.value = true

                                    coroutineScope.launch {
                                        val result = snackBarHostState.showSnackbar(
                                            message = "$removedItem removed",
                                            actionLabel = "Undo",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            activeItems.add(index, removedItem)
                                            deletedItems.remove(removedItem)
                                        }
                                    }
                                }
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
                            },
                            content = {
                                Box(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(16.dp)
                                ) {
                                    Text(text = item)
                                }
                            },
                            modifier = modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                LazyColumn {
                    item {
                        Text(
                            text = "Deleted Items",
                            modifier = modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    items(deletedItems) { item ->
                        ListItem(
                            headlineContent = {
                                Text(text = item)

                            },
                            modifier = modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF0F0F0))
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwipeToDeleteListPreview() {
    SwipeToDeleteList()
}