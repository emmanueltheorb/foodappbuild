package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orb.bmdadmin.ui.theme.RobotoFontFamily

@Composable
fun TextFieldNormal(
    modifier: Modifier = Modifier,
    textValue: MutableState<String>,
    labelValue: String
) {
    Card(
        modifier = modifier
            .height(40.dp)
            .widthIn(55.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(7.dp)
            ),
        shape = RoundedCornerShape(7.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        MyTextField(
            placeholder = labelValue,
            textValue = textValue
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    textValue: MutableState<String>,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    BasicTextField(
        value = textValue.value,
        onValueChange = {
            textValue.value = it
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        maxLines = 1,
        cursorBrush = SolidColor(
            MaterialTheme.colorScheme.onBackground
        ),
        decorationBox = { innerTextField ->
            Box {
                if (textValue.value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                innerTextField()
            }
        },
        keyboardOptions = keyboardOptions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyIntTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    textValue: MutableState<Int>,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    BasicTextField(
        value = if (textValue.value == 0) {
            ""
        } else {
            textValue.value.toString()
        },
        onValueChange = {
            textValue.value = it.toIntOrNull() ?: 0
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        maxLines = 1,
        cursorBrush = SolidColor(
            MaterialTheme.colorScheme.onBackground
        ),
        decorationBox = { innerTextField ->
            Box {
                if (textValue.value <= 0) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                innerTextField()
            }
        },
        keyboardOptions = keyboardOptions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNullableTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    textValue: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    BasicTextField(
        value = textValue,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = RobotoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        maxLines = 1,
        cursorBrush = SolidColor(
            MaterialTheme.colorScheme.onBackground
        ),
        decorationBox = { innerTextField ->
            Box {
                if (textValue.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                innerTextField()
            }
        },
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun MyTextFieldWithCallback(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val textValue = remember { mutableStateOf("") }

    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        maxLines = 1,
        cursorBrush = SolidColor(
            MaterialTheme.colorScheme.onBackground
        ),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                innerTextField()
            }
        },
        keyboardOptions = keyboardOptions
    )
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun TextFieldNormalPreview() {
//    AppTheme {
//        Column {
//            Spacer(Modifier.height(100.dp))
//            TextFieldNormal(labelValue = "Food Name")
//        }
//    }
//}
