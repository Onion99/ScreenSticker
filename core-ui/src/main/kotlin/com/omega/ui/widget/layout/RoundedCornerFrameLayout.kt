package com.omega.ui.widget.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout


class RoundedCornerFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    var radius = 0f
    private lateinit var pathLayout: Path


    override fun dispatchDraw(canvas: Canvas) {
        if(!::pathLayout.isInitialized){
            super.dispatchDraw(canvas)
            return
        }
        val path = this.pathLayout;
        val save = canvas!!.save()
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val path = Path()
        val rectF = RectF(FLEX_GROW_DEFAULT,FLEX_GROW_DEFAULT, w.toFloat(), h.toFloat())
        val drawRadius = radius
        path.addRoundRect(rectF, drawRadius, drawRadius, Path.Direction.CW)
        pathLayout = path
    }
}
const val FLEX_BASIS_PERCENT_DEFAULT = -1.0f
const val FLEX_GROW_DEFAULT = 0.0f
const val FLEX_SHRINK_DEFAULT = 1.0f
