package com.orb.bmdadmin.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MergedGroupState(initialIsMergedGroupModeEnabled: Boolean = false) {
    var isMergedGroupModeEnabled by mutableStateOf(initialIsMergedGroupModeEnabled)
}

object MergedGroupStateSaver : Saver<MergedGroupState, Boolean> {
    override fun restore(value: Boolean): MergedGroupState {
        return MergedGroupState(value)
    }

    override fun SaverScope.save(value: MergedGroupState): Boolean? {
        return value.isMergedGroupModeEnabled
    }
}

@Composable
fun rememberMergedGroupState(): MergedGroupState {
    return rememberSaveable(saver = MergedGroupStateSaver) {
        MergedGroupState()
    }
}

@Composable
fun <T> MergedGroup(
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
    if (items.isNotEmpty()) {
        Card(
            modifier.wrapContentSize(),
            shape = RoundedCornerShape(13.dp),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = modifier.padding(8.dp),
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
    } else {
        null
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MergedContainer(
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