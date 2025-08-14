/*
 * Copyright 2019 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */
package com.omega.ui.splitties

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import splitties.views.dsl.core.NO_THEME

typealias NewViewWithStyleAttrRef<V> = (Context, AttributeSet?, Int) -> V
typealias NewVieConstructRef<V> = (Context) -> V

//inline operator fun <reified V : View> XmlStyle<V>.invoke(
//    ctx: Context,
//    @IdRes id: Int = View.NO_ID,
//    @StyleRes theme: Int = NO_THEME,
//    initView: V.() -> Unit = {}
//): V = ctx.viewFactory.getThemeAttrStyledView<V>(ctx.wrapCtxIfNeeded(theme), null, styleAttr).also {
//    it.id = id
//}.apply(initView)

//inline fun <V : View> Context.styledView(
//    newViewRef: NewViewWithStyleAttrRef<V>,
//    @AttrRes styleAttr: Int,
//    @IdRes id: Int = View.NO_ID,
//    @StyleRes theme: Int = NO_THEME,
//    initView: V.() -> Unit = {}
//): V = newViewRef(this.wrapCtxIfNeeded(theme), null, styleAttr).also {
//    it.id = id
//}.apply(initView)
inline fun <V : View> Context.styledView(
    newViewRef: NewVieConstructRef<V>,
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: V.() -> Unit = {}
): V = newViewRef(this).also {
    it.id = id
}.apply(initView)
