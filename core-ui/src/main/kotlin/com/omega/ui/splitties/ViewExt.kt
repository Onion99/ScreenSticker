package com.omega.ui.splitties

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.MessageQueue
import android.os.SystemClock
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.doOnDetach
import androidx.recyclerview.widget.RecyclerView
import com.facebook.common.util.UriUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.Priority
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.omega.resource.R
import com.omega.ui.drawabletoolbox.DrawableBuilder
import splitties.dimensions.dip
import splitties.views.isConfigRtl


inline val String.color get() = Color.parseColor(this)

// ------------------------------------------------------------------------
// 改变View可见性
// ------------------------------------------------------------------------
fun View?.changeVisibility(isVisible: Boolean) {
    if (this == null) return
    if (isVisible && visibility == View.VISIBLE) return
    if (!isVisible && visibility == View.GONE) return
    visibility = if (isVisible) View.VISIBLE else View.GONE
}
fun View?.changeVisibilityNoGone(isVisible: Boolean) {
    if (this == null) return
    if (isVisible && visibility == View.VISIBLE) return
    if (!isVisible && visibility == View.INVISIBLE) return
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}


fun View.removeSelfFromParent() {
    if (this.parent != null && this.parent is ViewGroup) {
        (this.parent as ViewGroup).removeView(this)
    }
}

/**
 * 是否在控件区域内
 */
fun View.inRangeOfView(event: MotionEvent): Boolean {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    val x = location[0]
    val y = location[1]
    if (event.rawX < x || event.rawX > x + this.width || event.rawY < y || event.rawY > (y + this.height)) {
        return false
    }
    return true
}

// ------------------------------------------------------------------------
// 添加波纹背景 - view没有背景，用背景
// ------------------------------------------------------------------------
fun View.addBackgroundRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}
// ------------------------------------------------------------------------
// 添加波纹背景 - view没有背景，用背景-圆角波纹
// ------------------------------------------------------------------------

fun View.addBackgroundCircleRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, this, true)
    setBackgroundResource(resourceId)
}
// ------------------------------------------------------------------------
// 添加波纹背景 - view没有背景，用背景
// ------------------------------------------------------------------------

fun View.addForegroundRipple() = with(TypedValue()) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@with
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    foreground = ContextCompat.getDrawable(context, resourceId)
}
// ------------------------------------------------------------------------
// 添加波纹背景 - view没有背景，用背景-圆角波纹
// ------------------------------------------------------------------------

fun View.addForegroundCircleRipple() = with(TypedValue()) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@with
    context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, this, true)
    foreground = ContextCompat.getDrawable(context, resourceId)
}

// ------------------------------------------------------------------------
// 安全使用textCursorDrawable
// ------------------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.Q)
fun AppCompatEditText.setSafeTextCursor(drawable: Drawable?) = apply {
    kotlin.runCatching {
        textCursorDrawable = drawable
    }
}

// ------------------------------------------------------------------------
// 安全使用setSafeForeground
// ------------------------------------------------------------------------
@SuppressLint("ObsoleteSdkInt")
fun View.setSafeForeground(drawable: Drawable?, @ColorInt tintColor: Int) = apply {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        backgroundTintList = ColorStateList.valueOf(tintColor)
    } else foreground = drawable
}

// ------------------------------------------------------------------------
// 获取状�?�栏高度
// ------------------------------------------------------------------------
private var statusBarHeight = 0
fun getStatusBarHeight(context: Context): Int {
    if(statusBarHeight > 0) return  statusBarHeight
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    statusBarHeight =  if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else context.dip(30)
    return  statusBarHeight
}

// ------------------------------------------------------------------------
// 获取导航栏高�?
// ------------------------------------------------------------------------
fun getNavigationBarHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 30
}

// ------------------------------------------------------------------------
// TextView Style
// ------------------------------------------------------------------------
fun TextView.setStartDrawables(rid:Int) = apply {
    val drawable= getDrawable(rid) ?: return@apply
    drawable.setBounds(30,0,drawable.intrinsicWidth,drawable.intrinsicHeight)
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null,null,null)
}

fun TextView.setStartDrawables(drawable:Drawable?) = apply {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null,null,null)
}
fun TextView.setEndDrawables(rid:Int) = apply {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,getDrawable(rid),null)
}
fun TextView.setTopDrawables(rid:Int) = apply {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(rid),null,null)
}
fun TextView.setBottomDrawables(rid:Int) = apply {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,null,getDrawable(rid))
}

fun TextView.setBottomDrawables(drawable:Drawable?) = apply {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,null,drawable)
}

// ------------------------------------------------------------------------
// 监听是否绘制�?
// ------------------------------------------------------------------------
inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

// ------------------------------------------------------------------------
// To get view resource
// ------------------------------------------------------------------------
fun View.getColor(@ColorRes color: Int): Int = ContextCompat.getColor(context, color)
fun View.getDrawable(id: Int): Drawable? = ContextCompat.getDrawable(context, id)
fun View.getColorStateList(rid: Int) = AppCompatResources.getColorStateList(this.context, rid)
fun View.getString(@StringRes resourcesId:Int) = context.getString(resourcesId)
fun View.getString(@StringRes resourcesId:Int,vararg value:String) = context.getString(resourcesId,*value)

// ------------------------------------------------------------------------
// normal view background create
// ------------------------------------------------------------------------
fun View.drawableWithColorRadius(@ColorRes solidColor: Int, radius: Int,ripple:Boolean=true, @ColorRes rippleColor: Int=R.color.theme_color_primary_bubble): Drawable {
    return DrawableBuilder()
        .rectangle()
        .cornerRadius(radius)
        .rippleColor(getColor(rippleColor))
        .ripple(ripple)
        .solidColor(getColor(solidColor))
        .build()
}



fun View.drawableWithColorRadiusTop(solidColor: Int, radius: Int): Drawable {
    return DrawableBuilder()
        .rectangle()
        .topLeftRadius(radius)
        .topRightRadius(radius)
        .solidColor(getColor(solidColor))
        .build()
}

fun View.drawableWithColorRadiusBottom(solidColor: Int, radius: Int): Drawable {
    return DrawableBuilder()
        .rectangle()
        .bottomLeftRadius(radius)
        .bottomRightRadius(radius)
        .solidColor(getColor(solidColor))
        .build()
}

fun View.drawableWithColorRadiusRight(solidColor: Int, radius: Int,ripple:Boolean=true): Drawable {
    return DrawableBuilder()
        .rectangle()
        .apply {
            if(isConfigRtl){
                topLeftRadius(radius)
                bottomLeftRadius(radius)
            }else {
                topRightRadius(radius)
                bottomRightRadius(radius)
            }
        }
        .rippleColor(getColor(R.color.theme_color_background))
        .ripple(ripple)
        .solidColor(getColor(solidColor))
        .build()
}

fun View.drawableWithColorRadiusLeft(solidColor: Int, radius: Int,ripple:Boolean=true): Drawable {
    return DrawableBuilder()
        .rectangle()
        .apply {
            if(isConfigRtl){
                topRightRadius(radius)
                bottomRightRadius(radius)
            }else{
                topLeftRadius(radius)
                bottomLeftRadius(radius)
            }
        }
        .rippleColor(getColor(R.color.theme_color_background))
        .ripple(ripple)
        .solidColor(getColor(solidColor))
        .build()
}


fun View.drawableLine(solidColor: Int, width:Int,height: Int): Drawable {
    return DrawableBuilder()
        .rectangle()
        .size(width,height)
        .solidColor(getColor(solidColor))
        .build()
}

fun View.drawableLineRadius(solidColor: Int, width:Int,height: Int, radius: Int): Drawable {
    return DrawableBuilder()
        .rectangle()
        .cornerRadius(radius)
        .size(width,height)
        .solidColor(getColor(solidColor))
        .build()
}


fun View.drawableWithColorRadiusStoke(
    solidColor: Int,
    radius: Int,
    strokeColor: Int,
    strokeWidth: Int = dip(1),
    ripple: Boolean = true,

    ): Drawable {
    return DrawableBuilder()
        .rectangle()
        .cornerRadius(radius)
        .strokeWidth(strokeWidth)
        .strokeColor(getColor(strokeColor))
        .rippleColor(getColor(R.color.theme_color_primary))
        .ripple(ripple)
        .solidColor(getColor(solidColor))
        .build()
}

fun View.drawableWithStoke(
    radius: Int,
    strokeColor: Int,
    strokeWidth: Int = dip(1),
    ripple: Boolean = true,

    ): Drawable {
    return DrawableBuilder()
        .rectangle()
        .cornerRadius(radius)
        .strokeWidth(strokeWidth)
        .strokeColor(getColor(strokeColor))
        .rippleColor(getColor(R.color.theme_color_primary))
        .ripple(ripple)
        .build()
}

fun View.drawableWithStrokeSelected(
    radius: Int,
    selectedColor: Int,
    strokeColor: Int,
    strokeWidth: Int = dip(1),
): Drawable {
    return DrawableBuilder()
        .rectangle()
        .cornerRadius(radius)
        .strokeWidth(strokeWidth)
        .strokeColor(strokeColor)
        .solidColorSelected(selectedColor)
        .rippleColor(getColor(R.color.text_color_thirdary))
        .ripple(true)
        .build()
}


// ------------------------------------------------------------------------
// load online image
// ------------------------------------------------------------------------
private const val RESIZE_IMAGE_MIN = 99
private val viewHandleTimeOut = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) 899L else 1299L
fun SimpleDraweeView.load(url:String?,targetWidth:Int = 29,targetHeight:Int = 29,controllerListener: ControllerListener<ImageInfo> ? = null,isBlur:Boolean = false,stopScale:Boolean = false,timeout: Long = viewHandleTimeOut) {
    if(timeout < 100){
        post {
            kotlin.runCatching {
                var realWidth:Int = (height/ 1.5).toInt()
                var realHeight:Int = ( width/ 1.5).toInt()
                if(targetWidth  > 29) realHeight = targetWidth
                if(targetHeight > 29) realWidth  = targetHeight
                val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(if(stopScale) url else ScaleImageToNewUrl(url,realWidth,realHeight,isBlur)))
                    .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
                    .setResizeOptions(ResizeOptions(realWidth.coerceAtLeast(RESIZE_IMAGE_MIN), realHeight.coerceAtLeast(RESIZE_IMAGE_MIN)))
                    .setRequestPriority(Priority.MEDIUM)
                    .build()
                val controller: DraweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setOldController(controller)
                    .setControllerListener(controllerListener)
                    .build()
                this.controller = controller
            }
        }
    }else addIdleHandle(timeout){
        kotlin.runCatching {
            var realWidth:Int = (height/ 1.5).toInt()
            var realHeight:Int = ( width/ 1.5).toInt()
            if(targetWidth  > 29) realHeight = targetWidth
            if(targetHeight > 29) realWidth  = targetHeight
            val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(if(stopScale) url else ScaleImageToNewUrl(url,realWidth,realHeight,isBlur)))
                .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
                .setResizeOptions(ResizeOptions(realWidth.coerceAtLeast(RESIZE_IMAGE_MIN), realHeight.coerceAtLeast(RESIZE_IMAGE_MIN)))
                .setRequestPriority(Priority.MEDIUM)
                .build()
            val controller: DraweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(controller)
                .setControllerListener(controllerListener)
                .build()
            this.controller = controller
        }
    }
}

// ------------------------------------------------------------------------
// load local image
// ------------------------------------------------------------------------
fun SimpleDraweeView.load(url:Int,targetWidth:Int = 29,targetHeight:Int = 29,timeout: Long = viewHandleTimeOut) {
    addIdleHandle(timeout){
        kotlin.runCatching {
            var realWidth:Int = (height/ 1.5).toInt()
            var realHeight:Int = ( width/ 1.5).toInt()
            if(targetWidth  > 29) realHeight = targetWidth
            if(targetHeight > 29) realWidth  = targetHeight
            val imageRequest = ImageRequestBuilder.newBuilderWithSource(UriUtil.getUriForResourceId(url))
                .setCacheChoice(ImageRequest.CacheChoice.DEFAULT)
                .setResizeOptions(ResizeOptions(realWidth.coerceAtLeast(targetWidth), realHeight.coerceAtLeast(targetHeight)))
                .setRequestPriority(Priority.MEDIUM)
                .build()
            val controller: DraweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(controller)
                .build()
            this.controller = controller
        }
    }
}

// ------------------------------------------------------------------------
// Online Scale Image
// ------------------------------------------------------------------------
private fun ScaleImageToNewUrl(url: String?, targetWidth:Int, targetHeight:Int, isBlur:Boolean = false): String {
    if (url.isNullOrEmpty()) return ""
    if (url.startsWith("file:")) return url
    return url
}


// ------------------------------------------------------------------------
// avoid multi click
// ------------------------------------------------------------------------
private class DebounceAction(val view: View,  var block: ((View) -> Unit)): Runnable {
    override fun run() {
        if(view.isAttachedToWindow){
            block(view)
        }
    }
}

private fun throttleClick(wait: Long = 200, block: ((View) -> Unit)): View.OnClickListener {
    return View.OnClickListener { v ->
        val current = SystemClock.uptimeMillis()
        val lastClickTime = (v.getTag(R.id.qmui_click_timestamp) as? Long) ?: 0
        if (current - lastClickTime > wait) {
            v.setTag(R.id.qmui_click_timestamp, current)
            block(v)
        }
    }
}

private fun debounceClick(wait: Long = 200, block: ((View) -> Unit)): View.OnClickListener {
    return View.OnClickListener { v ->
        var action = (v.getTag(R.id.qmui_click_debounce_action) as? DebounceAction)
        if(action == null){
            action = DebounceAction(v, block)
            v.setTag(R.id.qmui_click_debounce_action, action)
        }else{
            action.block = block
        }
        v.removeCallbacks(action)
        v.postDelayed(action, wait)
    }
}

fun View.onClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(throttleClick(wait, block))
}

fun View.onDebounceClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(debounceClick(wait, block))
}


// ------------------------------------------------------------------------
// RecyclerView Safe CallBack
// ------------------------------------------------------------------------
fun RecyclerView.inScrollSafeNotify(action: (() -> Unit)){
    post {
        if(isComputingLayout.not()){
            action()
        }
    }
}

// ------------------------------------------------------------------------
// View Safe IdleHandle CallBack
// ------------------------------------------------------------------------
fun View.addIdleHandle(timeOut:Long,handle:()->Unit){
    post {
        var isDoAction = false
        val actionRunnable = Runnable{
            if(!isDoAction){
                isDoAction = true
                handle()
            }
        }
        val actionIdle = MessageQueue.IdleHandler{
            actionRunnable.run()
            false
        }
        doOnDetach {
            isDoAction = true
            removeCallbacks(actionRunnable)
            handler?.looper?.queue?.removeIdleHandler(actionIdle)
        }
        postDelayed(actionRunnable,timeOut)
        handler?.looper?.queue?.addIdleHandler(actionIdle)
    }
}
