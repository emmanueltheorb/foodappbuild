package com.orb.bmdadmin.ui.components.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.orb.bmdadmin.R
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.orb.bmdadmin.data.DropDownItem
import com.orb.bmdadmin.data.ProfileScreenViewModel
import com.orb.bmdadmin.ui.theme.AppTheme
import com.orb.bmdadmin.ui.theme.RobotoFontFamily
import com.orb.bmdadmin.ui.theme.SurfaceVariant
import com.orb.bmdadmin.ui.theme.SurfaceVariantLight
import com.orb.bmdadmin.ui.theme.White

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(),
    navToMessages: () -> Unit
) {
    val isAuthorized by profileScreenViewModel.isAuthorized.collectAsState()
    var code by remember { mutableIntStateOf(0) }
    val otp by profileScreenViewModel.otp.collectAsState()
    val currentUsername by profileScreenViewModel.currentUsername.collectAsState()
    var showPopUp by remember { mutableStateOf(false) }
    var usernameInput by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .padding(top = 10.dp)
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopBar(
                username = currentUsername,
                onUsernameClicked = {
                    if (currentUsername != "Username" && currentUsername != "username") {
                        usernameInput = currentUsername
                    }
                    showPopUp = true
                },
                onSystemClicked = {

                },
                onLightClicked = {

                },
                onDarkClicked = {

                },
                onLogOutClicked = {

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = modifier.size(50.dp),
                onClick = {

                },
                content = {
                    Icon(
                        modifier = modifier
                            .size(30.dp)
                            .padding(4.dp),
                        painter = painterResource(R.drawable.chat_icon),
                        contentDescription = "message button",
                        tint = White
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = modifier
                    .height(900.dp)
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isAuthorized) {
                    item(
                        content = {
                            Button(
                                onClick = {
                                    code = profileScreenViewModel.generateCode()
                                    profileScreenViewModel.storeCodeInDB(code)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Text(
                                    text = "Generate Code",
                                    color = MaterialTheme.colorScheme.background
                                )
                            }
                        }
                    )

                    item(
                        content = {
                            Text(
                                text = otp.toString(),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
            }
            if (showPopUp) {
                UsernamePopUp(
                    onDismissRequest = {
                        showPopUp = false
                    },
                    textValue = usernameInput,
                    onValueChange = {
                        usernameInput = it
                    },
                    onCancelClicked = {
                        showPopUp = false
                    },
                    onContinueClicked = {
                        profileScreenViewModel.updateUsername(usernameInput)
                        showPopUp = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    username: String,
    onUsernameClicked: () -> Unit,
    onSystemClicked: () -> Unit,
    onLightClicked: () -> Unit,
    onDarkClicked: () -> Unit,
    onLogOutClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier.clickable(
                onClick = onUsernameClicked
            ),
            text = if (username.isEmpty()) "Username" else username,
            color = if (username.isEmpty()) SurfaceVariant else MaterialTheme.colorScheme.onBackground
        )
        Menu(
            onSystemClicked = onSystemClicked,
            onLightClicked = onLightClicked,
            onDarkClicked = onDarkClicked,
            onLogOutClicked = onLogOutClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsernamePopUp(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    textValue: String,
    onValueChange: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onContinueClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.background,
        confirmButton = {
            Button(
                modifier = modifier.width(132.dp),
                onClick = onContinueClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(text = "Continue")
            }
        },
        dismissButton = {
            Button(
                modifier = modifier.width(132.dp),
                onClick = onCancelClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceVariantLight,
                    contentColor = White
                )
            ) {
                Text(text = "Cancel")
            }
        },
        text = {
            OutlinedCard(
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(13.dp),
                border = BorderStroke(width = 2.dp, color = SurfaceVariant),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                )
            ) {
                BasicTextField(
                    modifier = modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(10.dp),
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
                                    text = "Input username",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun Menu(
    modifier: Modifier = Modifier,
    onSystemClicked: () -> Unit,
    onLightClicked: () -> Unit,
    onDarkClicked: () -> Unit,
    onLogOutClicked: () -> Unit
) {
    var mainMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { mainMenuExpanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Main Dropdown Menu
        DropdownMenu(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background),
            expanded = mainMenuExpanded,
            onDismissRequest = {
                mainMenuExpanded = false
            }
        ) {
            MenuScreen(
                onSystemClicked = onSystemClicked,
                onLightClicked = onLightClicked,
                onDarkClicked = onDarkClicked,
                onLogOutClicked = onLogOutClicked
            )
        }
    }
}

@Composable
fun ExpandableItem(
    modifier: Modifier = Modifier,
    title: String,
    dropdownItems: List<DropDownItem>,
    isInitiallyExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(isInitiallyExpanded) }

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .semantics {
                    contentDescription = if (isExpanded) "Collapse $title" else "Expand $title"
                }
                .background(if (isExpanded) Color.LightGray else Color(0xFFF0F0F0))
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title)
            Spacer(modifier = modifier.weight(1f))
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = modifier
                    .background(Color.LightGray)
            ) {
                dropdownItems.forEach { item ->
                    Text(
                        text = item.text,
                        modifier = modifier
                            .fillMaxWidth()
                            .clickable(onClick = item.onClick)
                            .padding(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    onSystemClicked: () -> Unit,
    onLightClicked: () -> Unit,
    onDarkClicked: () -> Unit,
    onLogOutClicked: () -> Unit
) {
    Card(
        modifier = modifier
            .width(100.dp),
        shape = RoundedCornerShape(9.dp)
    ) {
        ExpandableItem(
            title = "Theme",
            dropdownItems = listOf(
                DropDownItem(
                    text = "System",
                    onClick = onSystemClicked
                ),
                DropDownItem(
                    text = "Light",
                    onClick = onLightClicked
                ),
                DropDownItem(
                    text = "Dark",
                    onClick = onDarkClicked
                )
            )
        )
        Text(
            text = "Log-out",
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0))
                .padding(6.dp)
                .clickable(onClick = onLogOutClicked)
        )
    }
}