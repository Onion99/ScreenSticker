package com.omega.ui.splitties

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import splitties.dimensions.dip
import splitties.views.bottomPadding
import splitties.views.dsl.constraintlayout.parentId
import splitties.views.dsl.core.wrapContent
import splitties.views.existingOrNewId
import splitties.views.horizontalPadding
import splitties.views.topPadding
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import com.google.android.material.appbar.CollapsingToolbarLayout.LayoutParams as LP


inline val Any.matchParent
    get() = ViewGroup.LayoutParams.MATCH_PARENT

/**
 * **A LESS CAPITALIZED ALIAS** to [ViewGroup.LayoutParams.WRAP_CONTENT] that is only
 * visible inside [ViewGroup]s.
 */
@Suppress("unused")
inline val Any.wrapContent
    get() = ViewGroup.LayoutParams.WRAP_CONTENT


fun ViewGroup.setMatchWLayoutParams(){
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
}
fun ViewGroup.setLayoutParams(width:Int,height:Int){
    layoutParams = ViewGroup.LayoutParams(width,height)
}

// message
fun ViewGroup.doBubblePadding() = apply {
    horizontalPadding = dip(12)
    topPadding = dip(10)
    bottomPadding = dip(9)
}

@OptIn(ExperimentalContracts::class)
inline fun AppBarLayout.lParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    initParams: AppBarLayout.LayoutParams.() -> Unit = {}
): AppBarLayout.LayoutParams {
    contract { callsInPlace(initParams, InvocationKind.EXACTLY_ONCE) }
    return AppBarLayout.LayoutParams(width, height).apply(initParams)
}

@OptIn(ExperimentalContracts::class)
inline fun CoordinatorLayout.lParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    initParams: CoordinatorLayout.LayoutParams.() -> Unit = {}
): CoordinatorLayout.LayoutParams {
    return CoordinatorLayout.LayoutParams(width, height).apply(initParams)
}


fun ConstraintLayout.LayoutParams.topStartOnParent(){
    topToTop = parentId
    startToStart = parentId
}
fun ConstraintLayout.LayoutParams.topEndOnParent(){
    topToTop = parentId
    endToEnd = parentId
}

fun ConstraintLayout.LayoutParams.bottomEndOn(view: View) {
    val id = view.existingOrNewId
    endToEnd = id
    bottomToBottom = id
}
fun ConstraintLayout.LayoutParams.topStartOn(view: View) {
    val id = view.existingOrNewId
    startToStart = id
    topToTop = id
}
fun ConstraintLayout.LayoutParams.topEndOn(view: View) {
    val id = view.existingOrNewId
    endToEnd = id
    topToTop = id
}

fun ConstraintLayout.LayoutParams.topToBottomStartOn(view: View) {
    val id = view.existingOrNewId
    startToStart = id
    topToBottom = id
}

@OptIn(ExperimentalContracts::class)
inline fun CoordinatorLayout.defaultLParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    gravity: Int = Gravity.NO_GRAVITY,
    initParams: CoordinatorLayout.LayoutParams.() -> Unit = {}
): CoordinatorLayout.LayoutParams {
    contract { callsInPlace(initParams, InvocationKind.EXACTLY_ONCE) }
    return CoordinatorLayout.LayoutParams(width, height).also {
        it.gravity = gravity
    }.apply(initParams)
}

@OptIn(ExperimentalContracts::class)
inline fun CoordinatorLayout.appBarLParams(
    height: Int = wrapContent,
    initParams: CoordinatorLayout.LayoutParams.() -> Unit = {}
): CoordinatorLayout.LayoutParams {
    contract { callsInPlace(initParams, InvocationKind.EXACTLY_ONCE) }
    return defaultLParams(
        width = matchParent,
        height = height,
        initParams = initParams
    )
}

inline fun CollapsingToolbarLayout.defaultLParams(
    width: Int = matchParent,
    height: Int = wrapContent,
    initParams: LP.() -> Unit = {}
): LP = LP(width, height).apply(initParams)

@OptIn(ExperimentalContracts::class)
inline fun CollapsingToolbarLayout.defaultLParams(
    width: Int = matchParent,
    height: Int = wrapContent,
    collapseMode: Int = LP.COLLAPSE_MODE_OFF,
    parallaxMultiplier: Float = 0.5f, // Default value as of 27.1.1
    initParams: LP.() -> Unit = {}
): LP {
    contract { callsInPlace(initParams, InvocationKind.EXACTLY_ONCE) }
    return LP(width, height).also {
        it.collapseMode = collapseMode
        it.parallaxMultiplier = parallaxMultiplier
    }.apply(initParams)
}

@OptIn(ExperimentalContracts::class)
inline fun CoordinatorLayout.contentScrollingWithAppBarLParams(
    initParams: CoordinatorLayout.LayoutParams.() -> Unit = {}
): CoordinatorLayout.LayoutParams {
    contract { callsInPlace(initParams, InvocationKind.EXACTLY_ONCE) }
    return defaultLParams(matchParent, matchParent) {
        behavior = AppBarLayout.ScrollingViewBehavior()
        initParams()
    }
}
