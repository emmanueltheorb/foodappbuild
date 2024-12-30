package com.example.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.data.FoodItemState
import com.example.app.data.FoodItemsData
import com.example.app.data.OptionState
import com.example.app.data.PriceViewModel
import com.example.app.ui.components.sections.createNameList


@Composable
fun dropdownMenu(
    modifier: Modifier = Modifier,
    options: MutableList<OptionState>,
    mergedGroupNumber: Int,
    width: Dp = 125.dp
): Int {
    val mergedGroupNumber = mergedGroupNumber
    var mergeIndex by remember {
        mutableIntStateOf(0)
    }
    val nameList: MutableList<String> = createNameList(
        options = options,
        mergeGroupNumber = mergedGroupNumber
    )
    var selectedText by remember {
        mutableStateOf(nameList[0])
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }

    OutlinedCard(
        modifier = Modifier.padding(16.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(width)
                .height(50.dp)
                .padding(5.dp)
                .clickable {
                    isExpanded = true
                }
        ) {
            Text(
                text = selectedText,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
            Image(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                contentDescription = ""
            )
        }

        DropdownMenu(
            modifier = modifier.background(MaterialTheme.colorScheme.background),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            nameList.forEachIndexed { index, text ->
                DropdownMenuItem(
                    modifier = modifier.background(MaterialTheme.colorScheme.background),
                    colors = MenuItemColors(
                        textColor = MaterialTheme.colorScheme.onBackground,
                        leadingIconColor = MaterialTheme.colorScheme.onBackground,
                        trailingIconColor = MaterialTheme.colorScheme.onBackground,
                        disabledTextColor = MaterialTheme.colorScheme.onBackground,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onBackground
                    ),
                    text = {
                        Text(
                            text = text,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        selectedText = nameList[index]
                        isExpanded = false
                        mergeIndex = index
                    }
                )
            }
        }
    }
    return mergeIndex
}