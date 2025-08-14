package com.omega.ui.widget.recyclerView

import com.omega.recyclerview.RecyclerView
import android.graphics.Rect
import android.util.Log
import android.view.ViewGroup


class FlowLayoutManager : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.isPreLayout) {
            return
        }
        detachAndScrapAttachedViews(recycler)
        if (itemCount <= 0) {
            return
        }
        var offsetX = paddingLeft
        var offsetY = paddingTop

        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)
            measureChildWithMargins(view, 0, 0)
            val lp = view.layoutParams as RecyclerView.LayoutParams
            val w = getDecoratedMeasuredWidth(view) + lp.leftMargin + lp.rightMargin
            val h = getDecoratedMeasuredHeight(view) + lp.topMargin + lp.bottomMargin
            Log.d("Debug", "view w: $w, h: $h")
            if (offsetX + w > width - paddingRight) {
                offsetX = paddingLeft
                offsetY += h
            }
            layoutDecoratedWithMargins(view, offsetX, offsetY, offsetX + w, offsetY + h)
            offsetX += w
        }

    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun setMeasuredDimension(childrenBounds: Rect?, wSpec: Int, hSpec: Int) {
        super.setMeasuredDimension(childrenBounds, wSpec, hSpec)

        val width: Int
        val height: Int
        val horizontalPadding = paddingLeft + paddingRight
        val verticalPadding = paddingTop + paddingBottom

        val usedHeight = childrenBounds!!.height() + verticalPadding
        val usedWidth = childrenBounds.width() + horizontalPadding
        height = chooseSize(hSpec, usedHeight, minimumHeight)
        width = chooseSize(wSpec, usedWidth, minimumWidth)
        setMeasuredDimension(width, height)

    }


}
