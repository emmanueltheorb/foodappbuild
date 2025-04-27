package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orb.bmdadmin.R
import com.orb.bmdadmin.ui.theme.AppTheme

@Composable
fun MySnackBar(
    modifier: Modifier = Modifier,
    snackBarData: SnackbarData,
    action: @Composable (() -> Unit)? = null,
    dismissAction: @Composable (() -> Unit)? = null,
    actionOnNewLine: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.background,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor
) {
    val customVisuals = snackBarData.visuals as? CustomSnackBarVisuals
    val isError = customVisuals?.isError ?: false

    val iconContainerColor = if (isError) Color(0xFFC62828) else Color(0xFF24C45E)
    val icon = if (!isError) R.drawable.check_mark_icon else R.drawable.ic_close
    val iconColor = Color.White


    Row(
        modifier = modifier
            .height(70.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .wrapContentWidth()
            .background(containerColor, shape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier.width(10.dp))
        Text(
            text = snackBarData.visuals.message,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier.width(10.dp))
        if (snackBarData.visuals.actionLabel == null) {
            Surface(
                modifier
                    .size(30.dp),
                shape = CircleShape,
                color = iconContainerColor,
                contentColor = containerColor
            ) {
                Icon(
                    modifier = modifier.padding(3.dp),
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
        Spacer(modifier.width(10.dp))
    }
}

class CustomSnackBarVisuals(
    override val message: String,
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
    override val withDismissAction: Boolean,
    val isError: Boolean
) : SnackbarVisuals

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCustomSnackbar() {
    AppTheme(darkTheme = true) {
// Create a fake SnackbarData
        val fakeData = object : SnackbarData {
            override val visuals: SnackbarVisuals
                get() = object : SnackbarVisuals {
                    override val message: String = "Network error, please try again later"
                    override val actionLabel: String? = null
                    override val duration: SnackbarDuration = SnackbarDuration.Short
                    override val withDismissAction: Boolean = false
                }

            override fun dismiss() {}
            override fun performAction() {}
        }

        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
// Just preview the custom composable
            MySnackBar(
                snackBarData = fakeData
            )
        }
    }
}
