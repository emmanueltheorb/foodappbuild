package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.orb.bmdadmin.R

@Composable
fun MySnackBar(
    modifier: Modifier = Modifier,
    snackBarData: SnackbarData,
    action: @Composable (() -> Unit)? = null,
    dismissAction: @Composable (() -> Unit)? = null,
    actionOnNewLine: Boolean = false,
    shape: Shape = RoundedCornerShape(7.dp),
    containerColor: Color = Color(0xFF494949),
    contentColor: Color = Color.White,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
    isSuccess: Boolean = true
) {
    val iconContainerColor = if (isSuccess) Color(0xFF24C45E) else Color(0xFFC62828)
    val icon = if (isSuccess) R.drawable.check_mark_icon else R.drawable.ic_close
    val iconColor = Color.White

    Row(
        modifier = modifier
            .height(70.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .wrapContentWidth()
            .background(containerColor, shape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = snackBarData.visuals.message,
            color = contentColor
        )

        Spacer(modifier.width(35.dp))

        if (action == null) {
            Surface(
                modifier.size(20.dp),
                shape = CircleShape,
                contentColor = containerColor
            ) {
                Icon(
                    modifier = modifier.size(17.dp),
                    painter = painterResource(icon),
                    tint = iconColor,
                    contentDescription = null
                )
            }
        } else {
            snackBarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(
                    onClick = {
                        snackBarData.performAction()
                    }
                ) {
                    Text(actionLabel, color = actionContentColor)
                }
            }
        }
    }
}