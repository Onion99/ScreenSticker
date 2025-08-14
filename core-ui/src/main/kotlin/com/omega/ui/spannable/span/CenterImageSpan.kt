/*
 * Copyright (C) 2018 Drake, https://github.com/liangjingkanji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*


package com.omega.ui.spannable.span

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.net.Uri
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.Gravity
import android.widget.TextView
import androidx.core.text.getSpans
import java.lang.ref.WeakReference
import kotlin.math.max

*/
/**
 * 比官方[ImageSpan]更好用的图片显示Span
 *
 * 图片垂直对齐方式
 * 图片宽高且保持固定比�?
 * 图片水平间距
 * 图片显示文字
 * shape自�?�应文字
 * .9PNG自�?�应文字, 若图片模糊请关闭硬件加�??(机型设备问题)
 *
 * 默认图片垂直居中对齐文字, 使用[setAlign]可指�?
 *
 * �?应对更复杂的图片加载�?求请使用[GlideImageSpan], 例如高斯模糊,棱形等效�?
 *//*

class CenterImageSpan : ImageSpan {

    */
/** 图片宽度 *//*

    private var drawableWidth: Int = 0

    */
/** 图片高度 *//*

    private var drawableHeight: Int = 0

    */
/** 图片间距 *//*

    private var drawableMargin: Rect = Rect()

    */
/** 图片内间�? *//*

    private var drawablePadding = Rect()

    private var drawableRef: WeakReference<Drawable>? = null

    */
/** 文字显示区域 *//*

    private var textDisplayRect = Rect()

    */
/** 图片原始间距 *//*

    private var drawableOriginPadding = Rect()

    override fun getDrawable(): Drawable {
        return drawableRef?.get() ?: super.getDrawable().apply {
            setFixedRatioZoom()
            drawableRef = WeakReference(this)
        }
    }

    */
/** 设置等比例缩放图�? *//*

    private fun Drawable.setFixedRatioZoom() {
        val ratio = intrinsicWidth.toDouble() / intrinsicHeight
        var width = when {
            drawableWidth > 0 -> drawableWidth
            drawableWidth == -1 -> textDisplayRect.width()
            else -> intrinsicWidth
        }
        var height = when {
            drawableHeight > 0 -> drawableHeight
            drawableHeight == -1 -> textDisplayRect.height()
            else -> intrinsicHeight
        }

        if (drawableWidth != -1 && intrinsicWidth > intrinsicHeight) {
            height = (width / ratio).toInt()
        } else if (drawableHeight != -1 && intrinsicWidth < intrinsicHeight) {
            width = (height * ratio).toInt()
        }

        getPadding(drawableOriginPadding)
        width += drawablePadding.left + drawablePadding.right + drawableOriginPadding.left + drawableOriginPadding.right
        height += drawablePadding.top + drawablePadding.bottom + drawableOriginPadding.top + drawableOriginPadding.bottom

        if (this is NinePatchDrawable) {
            width = max(width, intrinsicWidth)
            height = max(height, intrinsicHeight)
        }
        bounds.set(0, 0, width, height)
    }

    constructor(drawable: Drawable) : super(drawable)
    constructor(drawable: Drawable, source: String) : super(drawable, source)
    constructor(context: Context, uri: Uri) : super(context, uri)
    constructor(context: Context, resourceId: Int) : super(context, resourceId)
    constructor(context: Context, bitmap: Bitmap) : super(context, bitmap)

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        if (textSize > 0) {
            paint.textSize = textSize.toFloat()
        }
        val fontMetrics = paint.fontMetricsInt
        if (drawableWidth == -1 || drawableHeight == -1) {
            val r = Rect()
            paint.getTextBounds(text.toString(), start, end, r)
            val resizeFontMetrics = paint.fontMetricsInt
            textDisplayRect.set(
                0,
                0,
                r.width(),
                resizeFontMetrics.descent - resizeFontMetrics.ascent
            )
        }
        val bounds = drawable.bounds
        val imageHeight = bounds.height()
        if (fm != null) {
            when (align) {
                Align.CENTER -> {
                    val fontHeight = fontMetrics.descent - fontMetrics.ascent
                    fm.ascent =
                        fontMetrics.ascent - (imageHeight - fontHeight) / 2 - drawableMargin.top
                    fm.descent = fm.ascent + imageHeight + drawableMargin.bottom
                }
                Align.BASELINE -> {
                    fm.ascent =
                        fontMetrics.descent - imageHeight - fontMetrics.descent - drawableMargin.top - drawableMargin.bottom
                    fm.descent = 0
                }
                Align.BOTTOM -> {
                    fm.ascent =
                        fontMetrics.descent - imageHeight - drawableMargin.top - drawableMargin.bottom
                    fm.descent = 0
                }
            }

            fm.top = fm.ascent
            fm.bottom = fm.descent
        }
        return bounds.right + drawableMargin.left + drawableMargin.right
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        canvas.save()
        val drawable = drawable
        val bounds = drawable.bounds
        val transY = when (align) {
            Align.CENTER -> (2 * y + paint.fontMetricsInt.ascent + paint.fontMetricsInt.descent) / 2 - bounds.bottom / 2 - drawableMargin.height() / 2
            Align.BASELINE -> top - drawableMargin.bottom
            Align.BOTTOM -> top - drawableMargin.bottom
        }
        canvas.translate(x + drawableMargin.left, transY.toFloat())
        drawable.draw(canvas)

        // draw text
        if (textVisibility) {
            canvas.translate(
                -drawablePadding.width() / 2F - drawableOriginPadding.right,
                -drawablePadding.height() / 2F + drawableOriginPadding.top
            )
            val textWidth = paint.measureText(text, start, end)
            val textDrawRect = Rect()
            val textContainerRect = Rect(bounds)
            Gravity.apply(
                textGravity,
                textWidth.toInt(),
                paint.textSize.toInt(),
                textContainerRect,
                textDrawRect
            )
            if (text is Spanned) {
                // draw text color
                text.getSpans<ForegroundColorSpan>(start, end).lastOrNull()?.let {
                    paint.color = it.foregroundColor
                }
            }

            canvas.drawText(
                text, start, end,
                (textDrawRect.left + textOffset.left - textOffset.right).toFloat() + (drawableOriginPadding.right + drawableOriginPadding.left) / 2,
                (textDrawRect.bottom - paint.fontMetricsInt.descent / 2 + textOffset.top - textOffset.bottom).toFloat() - (drawableOriginPadding.bottom + drawableOriginPadding.top) / 2,
                paint
            )
        }
        canvas.restore()
    }


    enum class Align {
        BASELINE,
        CENTER,
        BOTTOM
    }

    private var align: Align = Align.CENTER

    //<editor-fold desc="Image">
    */
/**
     * 设置图片垂直对其方式
     * 图片默认垂直居中对齐文字: [Align.CENTER]
     *//*

    fun setAlign(align: Align) = apply {
        this.align = align
    }

    */
/**
     * 设置图片宽高
     * 如果指定大于零�?�则会基于图片宽高中�?大�?�然后根据宽高比例固定缩放图�?
     * @param  width 指定图片宽度, -1 使用文字宽度, 0 使用图片原始宽度
     * @param  height 指定图片高度, -1 使用文字高度, 0 使用图片原始高度
     *//*

    @JvmOverloads
    fun setDrawableSize(width: Int, height: Int = width) = apply {
        this.drawableWidth = width
        this.drawableHeight = height
        drawableRef?.clear()
    }

    */
/** 设置图片水平间距 *//*

    @JvmOverloads
    fun setMarginHorizontal(left: Int, right: Int = left) = apply {
        drawableMargin.left = left
        drawableMargin.right = right
    }

    */
/** 设置图片水平间距 *//*

    @JvmOverloads
    fun setMarginVertical(top: Int, bottom: Int = top) = apply {
        drawableMargin.top = top
        drawableMargin.bottom = bottom
    }

    */
/**
     * 设置图片水平内间�?
     *//*

    @JvmOverloads
    fun setPaddingHorizontal(left: Int, right: Int = left) = apply {
        drawablePadding.left = left
        drawablePadding.right = right
        drawableRef?.clear()
    }

    */
/**
     * 设置图片垂直内间�?
     *//*

    @JvmOverloads
    fun setPaddingVertical(top: Int, bottom: Int = top) = apply {
        drawablePadding.top = top
        drawablePadding.bottom = bottom
        drawableRef?.clear()
    }
    //</editor-fold>

    //<editor-fold desc="Text">
    private var textOffset = Rect()
    private var textGravity = Gravity.CENTER
    private var textVisibility = false
    private var textSize = 0

    */
/**
     * 当前为背景图�?, 这会导致显示文字内容, 但图片不会根据文字内容自动调�?
     * @param visibility 是否显示文字
     *//*

    @JvmOverloads
    fun setTextVisibility(visibility: Boolean = true) = apply {
        this.textVisibility = visibility
    }

    */
/**
     * 文字偏移�?
     *//*

    @JvmOverloads
    fun setTextOffset(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) = apply {
        textOffset.set(left, top, right, bottom)
    }

    */
/**
     * 文字对齐方式(基于图片), 默认对齐方式[Gravity.CENTER]
     * @param gravity 值等效于[TextView.setGravity], 例如[Gravity.BOTTOM], 使用[or]组合多个�?
     *//*

    fun setTextGravity(gravity: Int) = apply {
        textGravity = gravity
    }

    */
/**
     * 配合[AbsoluteSizeSpan]设置字体大小则图�?/文字会基线对�?, 而使用本方法则图�?/文字会居中对�?
     * @param size 文字大小, 单位px
     * @see setTextVisibility
     *//*

    fun setTextSize(size: Int) = apply {
        textSize = size
    }
    //</editor-fold>

}*/
