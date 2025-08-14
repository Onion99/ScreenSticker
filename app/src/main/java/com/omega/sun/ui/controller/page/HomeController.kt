package com.omega.sun.ui.controller.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omega.resource.R
import com.omega.sun.ui.controller.base.BaseLifecycleController

// The controller remains the same.
class HomeController : BaseLifecycleController() {
    @Composable
    override fun ComposeUI() {
        // We assume you have a MaterialTheme wrapper around your app.
        // For preview purposes, I'll add one.
        YourAppTheme { // Replace with your actual theme
            HomeScreen()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            // A CenterAlignedTopAppBar feels more modern and balanced for a main screen.
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.home_title), style = MaterialTheme.typography.titleLarge) },
                // Let's add our beautiful app icon here!
                /*navigationIcon = {
                    Icon(
                        painter = painterResource(id = com.omega.sun.R.drawable.ic_launcher), // Assuming you have the icon
                        contentDescription = "App Icon",
                        modifier = Modifier.padding(start = 8.dp).size(32.dp),
                        tint = Color.Unspecified // Important: Use the original colors
                    )
                },*/
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            )
        },
        // Use the theme's background color for better dark mode support and consistency.
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        // LazyColumn is more efficient than a scrollable Column for lists.
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Consistent spacing between cards
        ) {
            // --- Text Note Section ---
            item {
                ActionCard(
                    title = stringResource(id = R.string.text_tip),
                    icon = Icons.Rounded.Edit
                ) {
                    var textState by remember { mutableStateOf("") }

                    // OutlinedTextField is a more modern M3 component.
                    OutlinedTextField(
                        value = textState,
                        onValueChange = { textState = it },
                        label = { Text(stringResource(id = R.string.text_edit_tip)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        placeholder = { Text("Jot down a quick thought...") },
                        leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Using different button styles for visual hierarchy.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // The primary action is a filled button.
                        Button(
                            onClick = { /* TODO: pasteText action */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.Home, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.paste_text_to_screen))
                        }
                        // The secondary action is an outlined button.
                        OutlinedButton(
                            onClick = { /* TODO: pasteClipboard action */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.Home, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.paste_clipboard_to_screen))
                        }
                    }
                }
            }

            // --- Image Note Section ---
            item {
                ActionCard(
                    title = stringResource(id = R.string.image_tip),
                    icon = Icons.Rounded.Home
                ) {
                    // FilledTonalButtons are great for secondary, contained actions.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { /* TODO: album action */ },
                            modifier = Modifier.weight(1f).height(56.dp)
                        ) {
                            Icon(Icons.Rounded.Home, contentDescription = null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.album))
                        }
                        FilledTonalButton(
                            onClick = { /* TODO: camera action */ },
                            modifier = Modifier.weight(1f).height(56.dp)
                        ) {
                            Icon(Icons.Rounded.Home, contentDescription = null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.camera))
                        }
                    }
                }
            }

            // --- Help Section ---
            item {
                ActionCard(
                    title = stringResource(id = R.string.help),
                    icon = Icons.Rounded.KeyboardArrowRight
                ) {
                    // Use theme colors, not hardcoded ones. onSurfaceVariant is perfect for secondary text.
                    Text(
                        text = stringResource(id = R.string.help_tip),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * A reusable, styled card for grouping actions on the screen.
 * This makes the main layout much cleaner and ensures a consistent design.
 */
@Composable
fun ActionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            // This is where the unique content (buttons, text fields) for each card will go.
            content()
        }
    }
}

// Dummy theme for previewing
@Composable
fun YourAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF4A5E9D), // Colors inspired by your first icon
            surface = Color(0xFFFDF8F0), // The "Canvas Cream" we designed
            surfaceVariant = Color(0xFFE8EAF6),
            onSurface = Color(0xFF1B1B1F),
            onSurfaceVariant = Color(0xFF44464F)
        ),
        content = content
    )
}





