package com.omega.ui.widget.recyclerView

import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView


// ------------------------------------------------------------------------
// SafeRecyclerView
// ------------------------------------------------------------------------
class SafeRecyclerView @JvmOverloads constructor(
    context: android.content.Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        kotlin.runCatching {
            super.onMeasure(widthSpec, heightSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        kotlin.runCatching {
            super.onLayout(changed, l, t, r, b)
        }
    }
}

