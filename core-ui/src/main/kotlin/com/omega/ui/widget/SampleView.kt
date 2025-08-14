package com.omega.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class SampleView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs,defStyleAttr){


    // ------------------------------------------------------------------------
    // prepare data
    // ------------------------------------------------------------------------
    init {

    }

    // ------------------------------------------------------------------------
    // location
    // ------------------------------------------------------------------------
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    // ------------------------------------------------------------------------
    // draw
    // ------------------------------------------------------------------------
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private lateinit var refreshRunnable:RefreshRunnable
    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (::refreshRunnable.isInitialized) removeCallbacks(refreshRunnable)
        if (VISIBLE == visibility){
            refreshRunnable = RefreshRunnable()
            post(refreshRunnable)
        }
    }

    // ------------------------------------------------------------------------
    // Android 系统每隔 16ms 会发出 VSYNC 信号，触发对 UI 进行渲染，如果每次都渲染成功，就能够达到流畅画面所需的 60PS
    // ------------------------------------------------------------------------
    inner class RefreshRunnable : Runnable {
        override fun run() {
            val start = System.currentTimeMillis()
            invalidate()
            val gap = 16 - (System.currentTimeMillis() - start)
            postDelayed(this, if (gap < 0) 0 else gap)
        }
    }
}