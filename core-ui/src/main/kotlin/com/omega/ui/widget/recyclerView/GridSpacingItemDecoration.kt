package com.omega.ui.widget.recyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by lengyacheng on 2023/2/11
 * desc
 */
class GridSpacingItemDecoration(
    val spanCount: Int,
    val horizonSpacing: Int,
    val verticalSpacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view!!, parent!!, state!!)
        outRect.left = horizonSpacing
        outRect.right = horizonSpacing
        outRect.bottom = verticalSpacing
        outRect.top = verticalSpacing
    }


}
