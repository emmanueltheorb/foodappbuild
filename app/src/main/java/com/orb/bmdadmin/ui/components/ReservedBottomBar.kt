package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReservedBottomBar(
    modifier: Modifier = Modifier,
    quantity: Int
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalDivider()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .navigationBarsPadding()
                .then(modifier.padding(horizontal = 18.dp))
                .heightIn(min = 56.dp)
        ) {
            Text(
                text = "Quantity",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = modifier.width(10.dp))
            Text(
                text = quantity.toString(),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = modifier.width(15.dp))
            Text(
                text = "24667821",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}