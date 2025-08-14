package com.omega.ui.splitties

import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.SeekBar
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.omega.ui.widget.*
import com.omega.ui.widget.image.photoview.PhotoDraweeView
import splitties.experimental.InternalSplittiesApi
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.view
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.cardView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: CardView.() -> Unit = {}
): CardView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::CardView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.cardView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: CardView.() -> Unit = {}
): CardView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.cardView(id, theme, initView)
}


@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.datePicker(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: DatePicker.() -> Unit = {}
): DatePicker {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::DatePicker,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.datePicker(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: DatePicker.() -> Unit = {}
): DatePicker {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.datePicker(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.photoDraweeView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: PhotoDraweeView.() -> Unit = {}
): PhotoDraweeView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::PhotoDraweeView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.photoDraweeView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: PhotoDraweeView.() -> Unit = {}
): PhotoDraweeView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.photoDraweeView(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.materialButton(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::MaterialButton,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.materialButton(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.materialButton(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.tabLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TabLayout.() -> Unit = {}
): TabLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::TabLayout,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.tabLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TabLayout.() -> Unit = {}
): TabLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.tabLayout(id, theme, initView)
}
