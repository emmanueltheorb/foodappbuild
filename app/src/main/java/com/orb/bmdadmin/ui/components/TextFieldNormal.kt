package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
//import com.orb.bmdadmin.ui.components.Direction
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun MySmallWidthTextField(
    modifier: Modifier = Modifier,
    placeholder: Int?,
    textValue: Int?,
    onValueChange: (Int?) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val value = remember { mutableStateOf(textValue) }
    
    OutlinedCard(
        modifier = Modifier.padding(3.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 2.5.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        BasicTextField(
            value = textValue?.toString() ?: "",
            onValueChange = {
                if (!it.isEmpty()) {
                    value.value = it.toInt()
                    onValueChange(it.toInt())
                } else {
                    value.value = null
                    onValueChange(null)
                }
            },
            modifier = modifier
                .wrapContentSize()
                .height(35.dp)
                .width(50.dp)
                .defaultMinSize(minWidth = 10.dp, minHeight = 10.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = RobotoFontFamily,
                fontSize = 24.sp
            ),
            maxLines = 1,
            cursorBrush = SolidColor(
                MaterialTheme.colorScheme.onBackground
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (value.value == null) {
                        Text(
                            modifier = modifier
                                .padding(3.dp)
                                .align(Alignment.Center),
                            text = placeholder.toString(),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = RobotoFontFamily,
                                fontSize = 24.sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MyLoginTextFieldPreview() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        MySmallWidthTextField(
            placeholder = 450,
            textValue = null,
            onValueChange = {}
        )
    }
}