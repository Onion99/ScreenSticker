package com.omega.sun.ui.controller.page.copy

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.omega.sun.ui.controller.base.BaseLifecycleController
import splitties.views.backgroundColor
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent

class TestTransparentController : BaseLifecycleController()  {

    private lateinit var  tvTestTitle:TextView
    override fun onCreateView(context: Context): View  = context.frameLayout {
        backgroundColor = Color.TRANSPARENT
        layoutParams = lParams(matchParent, matchParent)
    }
}
