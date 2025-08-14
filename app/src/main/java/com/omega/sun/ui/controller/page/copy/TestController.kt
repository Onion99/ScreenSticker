package com.omega.sun.ui.controller.page.copy

import android.content.Context
import android.view.View
import android.widget.TextView
import com.omega.sun.ui.controller.base.BaseLifecycleController
import com.omega.ui.drawabletoolbox.DrawableBuilder
import com.omega.ui.splitties.COLOR_222222
import com.omega.ui.splitties.COLOR_336B7280
import com.omega.ui.splitties.COLOR_80000000
import com.omega.ui.splitties.COLOR_E60B0B0C
import com.omega.ui.splitties.COLOR_F5F5F5
import com.omega.ui.splitties.topStartOnParent
import defaultFontTextView
import splitties.dimensions.dip
import splitties.views.backgroundColor
import splitties.views.bottomPadding
import splitties.views.dsl.constraintlayout.after
import splitties.views.dsl.constraintlayout.alignVerticallyOn
import splitties.views.dsl.constraintlayout.centerHorizontally
import splitties.views.dsl.constraintlayout.centerInParent
import splitties.views.dsl.constraintlayout.centerOn
import splitties.views.dsl.constraintlayout.constraintLayout
import splitties.views.dsl.constraintlayout.lParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.margin
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.wrapContent
import splitties.views.gravityBottom
import splitties.views.horizontalPadding
import splitties.views.verticalPadding

class TestController : BaseLifecycleController()  {

    private lateinit var  tvTestTitle:TextView
    override fun onCreateView(context: Context): View  = context.frameLayout {
        backgroundColor = COLOR_80000000
        layoutParams = lParams(matchParent, matchParent)
        setOnClickListener {
            router.popController(this@TestController)
        }

        add(constraintLayout {
            bottomPadding = dip(32)
            horizontalPadding = dip(32)
            verticalPadding = dip(32)
            background = DrawableBuilder().rectangle().solidColor(COLOR_F5F5F5).cornerRadii(dip(16),dip(16),0,0,).build()
            add(defaultFontTextView {
                tvTestTitle = this
                setTextColor(COLOR_222222)
                textSize = 18f
            }, lParams(wrapContent, wrapContent) {
                topStartOnParent()
                centerHorizontally()
                centerInParent()
                margin = dip(16)
            })
            add(defaultFontTextView {
                verticalPadding = dip(2)
                horizontalPadding = dip(6)
                setTextColor(COLOR_E60B0B0C)
                textSize = 10f
                background = DrawableBuilder().rectangle().cornerRadius(dip(16)).strokeWidth(dip(1))
                    .strokeColor(COLOR_336B7280).build()
            }, lParams(wrapContent, wrapContent) {
                alignVerticallyOn(tvTestTitle)
                after(tvTestTitle)
                centerOn(tvTestTitle)
            })
        }, lParams(matchParent, wrapContent, gravityBottom))
    }
}
