package com.omega.ui.widget.recyclerView

import android.content.Context
import android.util.AttributeSet
import com.omega.recyclerview.LinearLayoutManager
import com.omega.recyclerview.LinearSmoothScroller
import com.omega.recyclerview.RecyclerView

class MarqueeLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(recyclerView.context) {
                override fun calculateTimeForScrolling(dx: Int): Int {
                    return 200
                }
            }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    override fun canScrollVertically(): Boolean  = false
}
