package com.omega.ui.splitties

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import splitties.experimental.InternalSplittiesApi
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.view
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.circularProgressIndicator(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: CircularProgressIndicator.() -> Unit = {}
): CircularProgressIndicator {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::CircularProgressIndicator,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.circularProgressIndicator(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: CircularProgressIndicator.() -> Unit = {}
): CircularProgressIndicator {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.circularProgressIndicator(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.linearProgressIndicator(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: LinearProgressIndicator.() -> Unit = {}
): LinearProgressIndicator {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::LinearProgressIndicator,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.linearProgressIndicator(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: LinearProgressIndicator.() -> Unit = {}
): LinearProgressIndicator {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.linearProgressIndicator(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.progressBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ProgressBar.() -> Unit = {}
): ProgressBar {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::ProgressBar,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.progressBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ProgressBar.() -> Unit = {}
): ProgressBar {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.progressBar(id, theme, initView)
}
