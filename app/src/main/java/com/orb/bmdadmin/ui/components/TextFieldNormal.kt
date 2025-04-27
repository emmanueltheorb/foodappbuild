package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
//import com.orb.bmdadmin.ui.components.Direction
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Constraints.Companion
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.packInts
import androidx.compose.ui.util.unpackInt1
import com.orb.bmdadmin.ui.theme.RobotoFontFamily
import kotlin.text.isEmpty

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

@Composable
fun MyLoginTextField(
    modifier: Modifier = Modifier,
    placeholder: String,
    width: Dp = 260.dp,
    height: Dp = 60.dp,
    textValue: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedCard(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(width)
                .height(height)
                .padding(5.dp)
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                maxLines = 1,
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.onBackground
                ),
                visualTransformation = visualTransformation,
                decorationBox = { innerTextField ->
                    Box {
                        if (textValue.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.headlineMedium,
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySmallWidthTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: Int?,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var focused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .onFocusChanged { focused = it.isFocused }
            .heightIn(min = 35.dp)
            .width(50.dp),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = RobotoFontFamily,
            fontSize = 24.sp
        ),
        maxLines = 1,
        cursorBrush = if (focused) { // Show cursor only when focused
            SolidColor(MaterialTheme.colorScheme.onBackground)
        } else {
            SolidColor(Color.Transparent)
        },
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource, // Added interaction source
                placeholder = {
                    if (value.isEmpty() && !focused && placeholder != null) {
                        Text(
                            text = placeholder.toString(),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                fontSize = 24.sp,
                                fontFamily = RobotoFontFamily
                            )
                        )
                    }
                },
                label = {
                    placeholder?.let {
                        Text(
                            text = it.toString(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                // Custom border container
                container = {
                    Box(
                        Modifier
                            .border(
                                BorderStroke(2.5.dp, MaterialTheme.colorScheme.onBackground),
                                RoundedCornerShape(8.dp)
                            )
                    )
                },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    )
}

@Composable
fun OtpTextField(
    otpLength: Int = 8,
    onOtpComplete: (String) -> Unit = {}
) {
    // Holds the current OTP digits in a mutable list.
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    // Create a list of focus requesters for each OTP field.
    val focusRequesters = List(otpLength) { FocusRequester() }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        (0 until otpLength).forEach { index ->
            OutlinedTextField(
                value = otpValues[index],
                onValueChange = { newValue ->
                    // Allow only one digit and ensure it's a number.
                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        otpValues[index] = newValue
                        // Move focus to the next field if current field is not empty.
                        if (newValue.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                        // If all fields are filled, trigger the callback.
                        if (otpValues.joinToString("").length == otpLength) {
                            onOtpComplete(otpValues.joinToString(""))
                        }
                    }
                },
                modifier = Modifier
                    .width(25.dp)
                    .focusRequester(focusRequesters[index])
                    // Handle key events to support deletion and moving focus back.
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown &&
                            keyEvent.key == Key.Backspace &&
                            otpValues[index].isEmpty() &&
                            index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                        false
                    },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

//@JvmInline
//value class IntSize internal constructor(@PublishedApi internal val packedValue: Long) {
//    @Stable
//    val width: Int
//        get() = unpackInt1(packedValue)
//}
//
//enum class Direction {
//    Vertical, Horizontal, Both
//}
//class WrapContentNode(
//    var direction: Direction,
//    var unbounded: Boolean,
//    var alignmentCallback: (IntSize, LayoutDirection) -> IntOffset,
//) {
//    fun MeasureScope.measure(
//        measurable: Measurable,
//        constraints: Constraints
//    ): MeasureResult {
//        val wrappedConstraints = Constraints(
//            minWidth = if (direction != Direction.Vertical) 0 else constraints.minWidth,
//            minHeight = if (direction != Direction.Horizontal) 0 else constraints.minHeight,
//            maxWidth = if (direction != Direction.Vertical && unbounded) {
//                Constraints.Infinity
//            } else {
//                constraints.maxWidth
//            },
//            maxHeight = if (direction != Direction.Horizontal && unbounded) {
//                Constraints.Infinity
//            } else {
//                constraints.maxHeight
//            }
//        )
//        val placeable = measurable.measure(wrappedConstraints)
//        val wrapperWidth = placeable.width.coerceIn(constraints.minWidth, constraints.maxWidth)
//        val wrapperHeight = placeable.height.coerceIn(constraints.minHeight, constraints.maxHeight)
//        return layout(
//            wrapperWidth,
//            wrapperHeight
//        ) {
//            val position = alignmentCallback(
//                IntSize(wrapperWidth - placeable.width, wrapperHeight - placeable.height),
//                layoutDirection
//            )
//            placeable.place(position)
//        }
//    }
//}
//
//@Composable
//fun Modifier.getWidth(text: String): Dp {
//    Text(
//        modifier = Modifier.wrapContentSize(),
//        text = text
//    )
//    return 2.dp
//}