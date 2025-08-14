package com.omega.ui.splitties

import android.content.Context
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.omega.viewpager2.widget.ViewPager2
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.internal.FlowLayout
import com.omega.ui.widget.layout.NestedScrollableFrameLayout
import com.omega.ui.widget.layout.NestedScrollableLinearLayout
import com.omega.ui.widget.layout.RoundedCornerFrameLayout
import com.omega.ui.widget.recyclerView.AlphaRecyclerView
import com.omega.ui.widget.recyclerView.SafeRecyclerView
import com.omega.viewpager2.widget.ViewPager2 as NovaViewPage2
import splitties.experimental.InternalSplittiesApi
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.changeHandlerFrameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ChangeHandlerFrameLayout.() -> Unit = {}
): ChangeHandlerFrameLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::ChangeHandlerFrameLayout,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.changeHandlerFrameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ChangeHandlerFrameLayout.() -> Unit = {}
): ChangeHandlerFrameLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.changeHandlerFrameLayout(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.roundedCornerFrameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: RoundedCornerFrameLayout.() -> Unit = {}
): RoundedCornerFrameLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::RoundedCornerFrameLayout,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.roundedCornerFrameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: RoundedCornerFrameLayout.() -> Unit = {}
): RoundedCornerFrameLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.roundedCornerFrameLayout(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.viewPager(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ViewPager.() -> Unit = {}
): ViewPager {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::ViewPager,id, theme, initView)
}

@OptIn(ExperimentalContracts::class)
inline fun View.viewPager(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ViewPager.() -> Unit = {}
): ViewPager {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.viewPager(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.horizontalScrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: HorizontalScrollView.() -> Unit = {}
): HorizontalScrollView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::HorizontalScrollView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.horizontalScrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: HorizontalScrollView.() -> Unit = {}
): HorizontalScrollView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.horizontalScrollView(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.scrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ScrollView.() -> Unit = {}
): ScrollView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::ScrollView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.scrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ScrollView.() -> Unit = {}
): ScrollView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.scrollView(id, theme, initView)
}


@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.bottomNavigationView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: BottomNavigationView.() -> Unit = {}
): BottomNavigationView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::BottomNavigationView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.bottomNavigationView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: BottomNavigationView.() -> Unit = {}
): BottomNavigationView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.bottomNavigationView(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.viewPager2(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ViewPager2.() -> Unit = {}
): ViewPager2 {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::ViewPager2,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.viewPager2(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ViewPager2.() -> Unit = {}
): ViewPager2 {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.viewPager2(id, theme, initView)
}
@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.novaViewPager2(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: NovaViewPage2.() -> Unit = {}
): NovaViewPage2 {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::NovaViewPage2,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.novaViewPager2(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: NovaViewPage2.() -> Unit = {}
): NovaViewPage2 {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.novaViewPager2(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.materialCardView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialCardView.() -> Unit = {}
): MaterialCardView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::MaterialCardView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class)
inline fun View.materialCardView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialCardView.() -> Unit = {}
): MaterialCardView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.materialCardView(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.swipeRefreshLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SwipeRefreshLayout.() -> Unit = {}
): SwipeRefreshLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::SwipeRefreshLayout,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.swipeRefreshLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SwipeRefreshLayout.() -> Unit = {}
): SwipeRefreshLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.swipeRefreshLayout(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.safeRecyclerView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SafeRecyclerView.() -> Unit = {}
): SafeRecyclerView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::SafeRecyclerView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class)
inline fun View.safeRecyclerView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SafeRecyclerView.() -> Unit = {}
): SafeRecyclerView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.safeRecyclerView(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.alphaRecyclerView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AlphaRecyclerView.() -> Unit = {}
): AlphaRecyclerView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::AlphaRecyclerView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class)
inline fun View.alphaRecyclerView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AlphaRecyclerView.() -> Unit = {}
): AlphaRecyclerView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.alphaRecyclerView(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.coordinatorLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: CoordinatorLayout.() -> Unit = {}
): CoordinatorLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::CoordinatorLayout,id, theme, initView)
}

@OptIn(ExperimentalContracts::class)
inline fun View.coordinatorLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: CoordinatorLayout.() -> Unit = {}
): CoordinatorLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.coordinatorLayout(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.nestedScrollableFrameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: NestedScrollableFrameLayout.() -> Unit = {}
): NestedScrollableFrameLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::NestedScrollableFrameLayout,id, theme, initView)
}

@OptIn(ExperimentalContracts::class)
inline fun View.nestedScrollableFrameLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: NestedScrollableFrameLayout.() -> Unit = {}
): NestedScrollableFrameLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.nestedScrollableFrameLayout(id, theme, initView)
}
@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.nestedScrollableLinearLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: NestedScrollableLinearLayout.() -> Unit = {}
): NestedScrollableLinearLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::NestedScrollableLinearLayout,id, theme, initView)
}

@OptIn(ExperimentalContracts::class)
inline fun View.nestedScrollableLinearLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: NestedScrollableLinearLayout.() -> Unit = {}
): NestedScrollableLinearLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.nestedScrollableLinearLayout(id, theme, initView)
}

