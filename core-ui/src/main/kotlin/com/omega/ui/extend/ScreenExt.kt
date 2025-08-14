package com.omega.ui.extend

import android.content.Context
import android.view.View
import com.omega.ui.splitties.getStatusBarHeight


fun View.getScreenHeight() = resources.displayMetrics.heightPixels - getStatusBarHeight()
fun View.getScreenWidth() = resources.displayMetrics.widthPixels
// ------------------------------------------------------------------------
// 获取状�?�栏高度
// ------------------------------------------------------------------------
fun View.getStatusBarHeight(): Int = getStatusBarHeight(context)
// ------------------------------------------------------------------------
// 获取导航栏高�?
// ------------------------------------------------------------------------
fun View.getNavigationBarHeight(): Int {
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 30
}
