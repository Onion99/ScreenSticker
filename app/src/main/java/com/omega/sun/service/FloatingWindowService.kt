package com.omega.sun.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import coil.compose.AsyncImage
import kotlin.math.roundToInt

class FloatingWindowService : LifecycleService() {

    private lateinit var windowManager: WindowManager
    private var floatingWidget: View? = null

    // Create the owners that will control the lifecycle of the ComposeView
    private val lifecycleOwner = ServiceLifecycleOwner()
    private val viewModelStoreOwner = ServiceViewModelStoreOwner()
    private val savedStateRegistryOwner = ServiceSavedStateRegistryOwner(lifecycleOwner.lifecycle)

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        // Restore saved state before triggering the CREATED event
        savedStateRegistryOwner.performRestore(null)
        // Manually trigger the CREATED event
        lifecycleOwner.onCreate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val text = intent?.getStringExtra("EXTRA_TEXT")
        val imageUri = intent?.getParcelableExtra<Uri>("EXTRA_IMAGE_URI")
        val backgroundColor = intent?.getIntExtra("EXTRA_BACKGROUND_COLOR", 0) ?: 0
        val textColor = intent?.getIntExtra("EXTRA_TEXT_COLOR", 0) ?: 0


        if (floatingWidget == null) {
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = 100
                y = 100
            }

            floatingWidget = ComposeView(this).apply {
                // Attach the owners to the ComposeView's view tree
                setViewTreeLifecycleOwner(lifecycleOwner)
                setViewTreeViewModelStoreOwner(viewModelStoreOwner)
                setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)

                // This strategy is important for proper cleanup
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

                setContent {
                    FloatingWidget(
                        text = text,
                        imageUri = imageUri,
                        params = params,
                        backgroundColor = Color(backgroundColor),
                        textColor = Color(textColor),
                        onClose = { stopSelf() }
                    )
                }
            }
            windowManager.addView(floatingWidget, params)
            // Manually trigger the RESUMED event
            lifecycleOwner.onResume()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        floatingWidget?.let {
            windowManager.removeView(it)
        }
        // Manually trigger the DESTROYED event and clear the ViewModelStore
        lifecycleOwner.onDestroy()
        viewModelStoreOwner.clear()
    }

    @Composable
    private fun FloatingWidget(
        text: String?,
        imageUri: Uri?,
        params: WindowManager.LayoutParams,
        backgroundColor: Color,
        textColor: Color,
        onClose: () -> Unit
    ) {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            finishedListener = { if (!visible) onClose() }
        )
        val scale by animateFloatAsState(
            targetValue = if (visible) 1f else 0.8f
        )
        Box(
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { visible = false }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        params.x += dragAmount.x.roundToInt()
                        params.y += dragAmount.y.roundToInt()
                        windowManager.updateViewLayout(floatingWidget, params)
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor.copy(alpha = 0.8f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (text != null) {
                    Text(
                        text,
                        modifier = Modifier.padding(end = 8.dp),
                        color = textColor
                    )
                }
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

// Helper classes to provide the necessary context for Jetpack Compose

private class ServiceLifecycleOwner : LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    fun onCreate() = lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onResume() = lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onDestroy() = lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override val lifecycle: Lifecycle get() = lifecycleRegistry
}

private class ServiceViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
    fun clear() = viewModelStore.clear()
}

private class ServiceSavedStateRegistryOwner(override val lifecycle: Lifecycle) : SavedStateRegistryOwner {
    private val controller = SavedStateRegistryController.create(this)

    fun performRestore(savedState: Bundle?) {
        controller.performRestore(savedState)
    }

    override val savedStateRegistry: SavedStateRegistry get() = controller.savedStateRegistry
}
