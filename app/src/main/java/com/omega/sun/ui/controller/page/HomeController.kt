package com.omega.sun.ui.controller.page

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omega.resource.R
import com.omega.sun.service.FloatingWindowService
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
    val context = LocalContext.current
    var textState by remember { mutableStateOf("") }

    // Launcher to request overlay permission
    val overlayPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // After returning from settings, check permission again
        if (Settings.canDrawOverlays(context)) {
            // Permission granted, start the service with the current text
            if (textState.isNotBlank()) {
                val intent = Intent(context, FloatingWindowService::class.java).apply {
                    putExtra("EXTRA_TEXT", textState)
                }
                context.startService(intent)
            }
        } else {
            // TODO: Show a snackbar or toast that permission is required
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.home_title), style = MaterialTheme.typography.titleLarge) },
                /*navigationIcon = {
                    Icon(
                        painter = painterResource(id = com.omega.sun.R.drawable.ic_launcher), // Using the app icon
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
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Text Note Section ---
            item {
                ActionCard(
                    title = stringResource(id = R.string.text_tip),
                    icon = Icons.Rounded.Edit
                ) {
                    OutlinedTextField(
                        value = textState,
                        onValueChange = { textState = it },
                        label = { Text(stringResource(id = R.string.text_edit_tip)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        placeholder = { Text("Jot down a quick thought...") },
                        leadingIcon = { Icon(Icons.Rounded.Notes, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (textState.isBlank()) return@Button
                                if (Settings.canDrawOverlays(context)) {
                                    val intent = Intent(context, FloatingWindowService::class.java).apply {
                                        putExtra("EXTRA_TEXT", textState)
                                    }
                                    context.startService(intent)
                                } else {
                                    val intent = Intent(
                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:${context.packageName}")
                                    )
                                    overlayPermissionLauncher.launch(intent)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.Launch, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.paste_text_to_screen))
                        }
                        OutlinedButton(
                            onClick = { /* TODO: pasteClipboard action */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.ContentPaste, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
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
                    icon = Icons.Rounded.Image
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { /* TODO: album action */ },
                            modifier = Modifier.weight(1f).height(56.dp)
                        ) {
                            Icon(Icons.Rounded.PhotoLibrary, contentDescription = null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.album))
                        }
                        FilledTonalButton(
                            onClick = { /* TODO: camera action */ },
                            modifier = Modifier.weight(1f).height(56.dp)
                        ) {
                            Icon(Icons.Rounded.PhotoCamera, contentDescription = null)
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
                    icon = Icons.Rounded.HelpOutline
                ) {
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
            content()
        }
    }
}

@Composable
fun YourAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF4A5E9D),
            surface = Color(0xFFFDF8F0),
            surfaceVariant = Color(0xFFE8EAF6),
            onSurface = Color(0xFF1B1B1F),
            onSurfaceVariant = Color(0xFF44464F)
        ),
        content = content
    )
}
