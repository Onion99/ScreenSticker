package com.omega.sun.ui.controller.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omega.resource.R
import com.omega.sun.ui.controller.base.BaseLifecycleController

// Existing imports
// New imports
class HomeController : BaseLifecycleController() {

    @Composable
    override fun ComposeUI() {
        HomeLayout()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun HomeLayout() {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(stringResource(id = R.string.home_title)) })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState()),
            ) {
                // pasteSectionTitle1
                Text(
                    text = stringResource(id = R.string.text_tip),
                    style = MaterialTheme.typography.headlineSmall, // @style/TitleText
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )

                // editText
                var textState by remember { mutableStateOf("") }
                TextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text(stringResource(id = R.string.text_edit_tip)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .heightIn(min = 100.dp), // Similar to maxHeight: 200dp and gravity: start|top
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Default,
                        keyboardType = KeyboardType.Text
                    ),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { /* TODO: pasteText action */ }) {
                        Text(stringResource(id = R.string.paste_text_to_screen)) // @string/paste_on_screen
                    }
                    Button(onClick = { /* TODO: pasteClipboard action */ }) {
                        Text(stringResource(id = R.string.paste_clipboard_to_screen)) // @string/pasteTextButtonText
                    }
                }

                // paste_title_2
                Text(
                    text = stringResource(id = R.string.image_tip),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )

                // Buttons row 2
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { /* TODO: album action */ }) {
                        Text(stringResource(id = R.string.album)) // @string/album
                    }
                    Button(onClick = { /* TODO: camera action */ }) {
                        Text(stringResource(id = R.string.camera)) // @string/cameraText
                    }
                }

                Text(
                    text = stringResource(id = R.string.help),
                    style = MaterialTheme.typography.headlineSmall, // @style/TitleText
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )

                Text(
                    text = stringResource(id = R.string.help_tip),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF999999)), // textColor="#999999"
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f)) // Pushes the slider to the bottom

            }
        }
    }
}





