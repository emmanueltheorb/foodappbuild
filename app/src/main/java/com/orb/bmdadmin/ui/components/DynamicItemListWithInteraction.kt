package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DynamicItemListWithInteraction(
    modifier: Modifier = Modifier
) {
    val allItems = remember { mutableStateListOf("Apple") }
    val referenceList = remember { mutableStateMapOf<Int, List<String>>() }
    var keyCounter by remember { mutableIntStateOf(1) }
    var referenceItems by remember { mutableStateOf(emptyList<String>()) }
    var filteredItems by remember { mutableStateOf(allItems.toList()) }

    LaunchedEffect(allItems, referenceList) {
        snapshotFlow {
            Pair(allItems.toList(), referenceList.flatMap { it.value })
        }.collect { (updatedAllItems, updatedReferenceItems) ->
            referenceItems = updatedReferenceItems
            filteredItems = updatedAllItems.filterNot {
                it in referenceItems
            }
        }
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text("Filtered Items:", style = MaterialTheme.typography.headlineMedium)
        filteredItems.forEach { item ->
            Text(text = item)
        }
        Spacer(modifier = modifier.height(16.dp))
        Button(
            onClick = {
                val newItems = listOf("Date", "Fig").shuffled().take(1)
                referenceList[keyCounter] = newItems
                keyCounter++
            }
        ) {
            Text("Add to Reference List")
        }
        Spacer(modifier = modifier.height(16.dp))
        Button(
            onClick = {
                if (referenceList.isNotEmpty()) {
                    val firstKey = referenceList.keys.first()
                    referenceList.remove(firstKey)
                }
            }
        ) {
            Text("Remove from Reference List")
        }
        Spacer(modifier = modifier.height(16.dp))
        Button(
            onClick = {
                val newItem = listOf("Cherry", "Date", "Elderberry", "Fig").shuffled().first()
                if (newItem !in allItems) {
                    allItems.add(newItem)
                }
            }
        ) {
            Text("Add to All Items")
        }
        Spacer(modifier = modifier.height(16.dp))
        Text("All Items:", style = MaterialTheme.typography.headlineMedium)
        allItems.forEach { item ->
            Text(text = item)
        }
        Spacer(modifier = modifier.height(16.dp))
        Text("Reference List:", style = MaterialTheme.typography.headlineMedium)
        referenceList.forEach { (key, value) ->
            Text("Key $key: $value")
        }
    }
}