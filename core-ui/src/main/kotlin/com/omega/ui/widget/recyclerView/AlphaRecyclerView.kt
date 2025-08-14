package com.omega.ui.widget.recyclerView

import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.RecyclerView


// ------------------------------------------------------------------------
// SafeRecyclerView
// ------------------------------------------------------------------------
class AlphaRecyclerView @JvmOverloads constructor(
    context: android.content.Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        kotlin.runCatching {
            super.onMeasure(widthSpec, heightSpec)
        }.getOrElse {
            Log.e("AlphaRecyclerView",it.message.toString())
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        kotlin.runCatching {
            super.onLayout(changed, l, t, r, b)
        }.getOrElse {
            Log.e("AlphaRecyclerView",it.message.toString())
        }
    }
}

