package com.orb.bmdadmin.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup

class MultiSelectionState(initialIsMultiSelectionModeEnabled: Boolean = false) {
    var isMultiSelectionModeEnabled by mutableStateOf(initialIsMultiSelectionModeEnabled)
}

object MultiSelectionStateSaver : Saver<MultiSelectionState, Boolean> {
    override fun restore(value: Boolean): MultiSelectionState {
        return MultiSelectionState(value)
    }

    override fun SaverScope.save(value: MultiSelectionState): Boolean {
        return value.isMultiSelectionModeEnabled
    }

}

@Composable
fun rememberMultiSelectionState(): MultiSelectionState {
    return rememberSaveable(saver = MultiSelectionStateSaver) {
        MultiSelectionState()
    }
}

@Composable
fun <T> MultiSelectionList(
    modifier: Modifier = Modifier,
    state: MultiSelectionState,
    items: List<T>,
    selectedItems: List<T>,
    onSelectedItems: (List<T>) -> Unit,
    itemContent: @Composable (T) -> Unit,
    key: ((T) -> Any)? = null,
    onClick: (T) -> Unit
) {
    onSelectedItems(selectedItems)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (item in items) {
            MultiSelectionContainer(
                isEnabled = state.isMultiSelectionModeEnabled,
                isSelected = selectedItems.contains(item),
                multiSelectionModeChange = {
                    state.isMultiSelectionModeEnabled = it
                },
                onClick = { onClick(item) }
            ) {
                itemContent(item)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MultiSelectionContainer(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    isSelected: Boolean,
    multiSelectionModeChange: (Boolean) -> Unit,
    radioButtonBackgroundColor: Color = MaterialTheme.colorScheme.background,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(50.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    multiSelectionModeChange(true)
                    onClick()
                }
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            modifier = modifier.background(radioButtonBackgroundColor),
            visible = isEnabled,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.surface,
                    unselectedColor = MaterialTheme.colorScheme.surface
                )
            )
        }
        content()
    }
}

@Composable
fun PopUpForMergeAndRemove(
    modifier: Modifier = Modifier,
    onMergeClicked: () -> Unit,
    onRemoveClicked: () -> Unit
) {
    var isVisible by remember {
        mutableStateOf(false)
    }

    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, 50),
        onDismissRequest = { isVisible }
    ) {
        Surface(
            modifier
                .wrapContentSize()
                .padding(5.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            shadowElevation = 3.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Merge",
                    modifier
                        .padding(18.dp)
                        .clickable(onClick = onMergeClicked),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Remove",
                    modifier
                        .padding(18.dp)
                        .clickable(onClick = onRemoveClicked),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun PopUpForDelete(
    modifier: Modifier = Modifier,
    onDeleteClicked: () -> Unit,
) {
    var isVisible by remember {
        mutableStateOf(false)
    }

    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, 50),
        onDismissRequest = { isVisible }
    ) {
        Surface(
            modifier
                .wrapContentSize()
                .padding(5.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            shadowElevation = 3.dp
        ) {
            Text(
                text = "Delete",
                modifier
                    .padding(18.dp)
                    .clickable(onClick = onDeleteClicked),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}