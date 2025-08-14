package com.omega.ui.splitties

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.omega.resource.R
import splitties.experimental.InternalSplittiesApi
import splitties.views.appcompat.configActionBar
import splitties.views.appcompat.homeAsUp
import splitties.views.dsl.appcompat.toolbar
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.add
import splitties.views.dsl.core.view
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


// ------------------------------------------------------------------------
// AppBarLayout
// ------------------------------------------------------------------------
@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.appBarLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AppBarLayout.() -> Unit = {}
): AppBarLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(id, theme, initView)
}

@OptIn(ExperimentalContracts::class)
inline fun View.appBarLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AppBarLayout.() -> Unit = {}
): AppBarLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.appBarLayout(id, theme, initView)
}


fun CoordinatorLayout.addDefaultAppBar(ctx: Context) {
    add(appBarLayout(theme = R.style.AppTheme_AppBarOverlay) {
        add(toolbar {
            popupTheme = R.style.AppTheme_PopupOverlay
            val activity = ctx as? AppCompatActivity ?: return@toolbar
            activity.setSupportActionBar(this)
            activity.configActionBar { homeAsUp = true }
        }, defaultLParams())
    }, appBarLParams())
}