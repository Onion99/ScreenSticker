package com.omega.sun.ui.controller.page

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omega.resource.R
import com.omega.sun.service.FloatingWindowService
import com.omega.sun.ui.controller.base.BaseLifecycleController
import com.tencent.mmkv.MMKV
import net.mm2d.color.chooser.compose.ColorChooserDialog
import kotlinx.coroutines.launch

private const val KEY_TEXT = "KEY_TEXT"
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
    var textState by remember { mutableStateOf(MMKV.defaultMMKV().decodeString(KEY_TEXT, "") ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var backgroundColor by remember { mutableStateOf(Color.LightGray.copy(alpha = 0.8f)) }
    var textColor by remember { mutableStateOf(Color.Black) }
    var bgColorDialogShow by rememberSaveable { mutableStateOf(false) }
    var textColorDialogShow by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Launcher to request overlay permission
    val overlayPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // After returning from settings, check permission again
        if (Settings.canDrawOverlays(context)) {
            // Permission granted, start the service with the current text or image
            if (textState.isNotBlank()) {
                val intent = Intent(context, FloatingWindowService::class.java).apply {
                    putExtra("EXTRA_TEXT", textState)
                    putExtra("EXTRA_BACKGROUND_COLOR", backgroundColor.toArgb())
                    putExtra("EXTRA_TEXT_COLOR", textColor.toArgb())
                }
                context.startService(intent)
            } else if (imageUri != null) {
                val intent = Intent(context, FloatingWindowService::class.java).apply {
                    putExtra("EXTRA_IMAGE_URI", imageUri)
                    putExtra("EXTRA_BACKGROUND_COLOR", backgroundColor.toArgb())
                    putExtra("EXTRA_TEXT_COLOR", textColor.toArgb())
                }
                context.startService(intent)
            }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.overlay_permission_required),
                    actionLabel = context.getString(R.string.grant)
                )
            }
        }
    }

    // Launcher for picking an image from the gallery
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                if (Settings.canDrawOverlays(context)) {
                    val intent = Intent(context, FloatingWindowService::class.java).apply {
                        putExtra("EXTRA_IMAGE_URI", imageUri)
                        putExtra("EXTRA_BACKGROUND_COLOR", backgroundColor.toArgb())
                        putExtra("EXTRA_TEXT_COLOR", textColor.toArgb())
                    }
                    context.startService(intent)
                } else {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    )
                    overlayPermissionLauncher.launch(intent)
                }
            }
        }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.home_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                        onValueChange = {
                            textState = it
                            MMKV.defaultMMKV().encode(KEY_TEXT, it)
                        },
                        label = { Text(stringResource(id = R.string.text_edit_tip)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        placeholder = { Text(stringResource(id = R.string.text_edit_tip2)) },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (textState.isBlank()) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = context.getString(R.string.empty),
                                            actionLabel = context.getString(R.string.grant),
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    return@Button
                                }
                                if (Settings.canDrawOverlays(context)) {
                                    val intent =
                                        Intent(context, FloatingWindowService::class.java).apply {
                                            putExtra("EXTRA_TEXT", textState)
                                            putExtra(
                                                "EXTRA_BACKGROUND_COLOR",
                                                backgroundColor.toArgb()
                                            )
                                            putExtra("EXTRA_TEXT_COLOR", textColor.toArgb())
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
                            Icon(
                                Icons.Rounded.Launch,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.paste_text_to_screen))
                        }
                        OutlinedButton(
                            onClick = {
                                val clipboard =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clipData = clipboard.primaryClip
                                if (clipData != null && clipData.itemCount > 0) {
                                    val textToPaste = clipData.getItemAt(0).text
                                    if (textToPaste != null) {
                                        textState = textToPaste.toString()
                                        MMKV.defaultMMKV().encode(KEY_TEXT, textState)
                                        // Now, launch the floating window
                                        if (Settings.canDrawOverlays(context)) {
                                            val intent = Intent(
                                                context,
                                                FloatingWindowService::class.java
                                            ).apply {
                                                putExtra("EXTRA_TEXT", textState)
                                                putExtra(
                                                    "EXTRA_BACKGROUND_COLOR",
                                                    backgroundColor.toArgb()
                                                )
                                                putExtra("EXTRA_TEXT_COLOR", textColor.toArgb())
                                            }
                                            context.startService(intent)
                                        } else {
                                            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
                                            overlayPermissionLauncher.launch(intent)
                                        }
                                    }else {
                                        if (textState.isBlank()) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = context.getString(R.string.empty),
                                                    actionLabel = context.getString(R.string.grant),
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Rounded.ContentPaste,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
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
                            onClick = {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                        ) {
                            Icon(Icons.Rounded.PhotoLibrary, contentDescription = null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.album))
                        }
                        FilledTonalButton(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.camera_no),
                                        actionLabel = context.getString(R.string.grant),
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
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
                    icon = Icons.AutoMirrored.Rounded.HelpOutline
                ) {
                    Text(
                        text = stringResource(id = R.string.help_tip),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            // --- Style Section ---
            item {
                ActionCard(
                    title = stringResource(id = R.string.style),
                    icon = Icons.Rounded.Style
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                bgColorDialogShow = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                        ) {
                            Icon(Icons.Rounded.Colorize, contentDescription = null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.background_color))
                        }
                        FilledTonalButton(
                            onClick = {
                                textColorDialogShow = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                        ) {
                            Icon(Icons.Rounded.TextFields, contentDescription = null)
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(id = R.string.text_color))
                        }
                    }
                }
            }
        }
    }
    if(bgColorDialogShow||textColorDialogShow){
        ColorChooserDialog(
            initialColor = Color(if(bgColorDialogShow) backgroundColor.value else textColor.value),
            onDismissRequest = {
                if(bgColorDialogShow) bgColorDialogShow = false
                else textColorDialogShow = false
            },
            onChooseColor = { color ->
                if(bgColorDialogShow) backgroundColor = color
                else textColor = color
            }
        )
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