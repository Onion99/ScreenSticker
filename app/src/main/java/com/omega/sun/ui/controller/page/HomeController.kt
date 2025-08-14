package com.omega.sun.ui.controller.page

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.omega.opengl.render.ColorRenderer
import com.omega.opengl.render.NativeColorRenderer
import com.omega.opengl.render.SimpleRender
import com.omega.resource.R
import com.omega.sun.ui.controller.base.BaseLifecycleController
import com.omega.ui.extend.requireActivity
import com.omega.ui.splitties.TEXT_COLOR_PRIMARY
import com.omega.ui.splitties.appBarLParams
import com.omega.ui.splitties.appBarLayout
import com.omega.ui.splitties.contentScrollingWithAppBarLParams
import com.omega.ui.splitties.defaultLParams
import com.omega.ui.splitties.materialCardView
import com.omega.ui.splitties.onClick
import com.omega.ui.splitties.scrollView
import com.omega.ui.splitties.wrapContent
import com.omega.ui.widget.celebrate.CelebrateView
import com.omega.ui.widget.switch.SwitchButtonView
import com.omega.ui.widget.wave.SimpleWaveView
import splitties.dimensions.dip
import splitties.views.bottomPadding
import splitties.views.centerText
import splitties.views.dsl.appcompat.toolbar
import splitties.views.dsl.core.NewViewRef
import splitties.views.dsl.core.add
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.margin
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.verticalLayout
import splitties.views.dsl.core.view
import splitties.views.gravityCenter
import splitties.views.gravityCenterHorizontal
import splitties.views.textAppearance
import com.google.android.material.R as MaterialR

class HomeController : BaseLifecycleController()  {

    private lateinit var  tvTestTitle:TextView
    override fun onCreateView(context: Context): View  = HomePageView(context)
}

class HomePageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :CoordinatorLayout(context, attrs,defStyleAttr){

    private val viewTitleMargin = 8
    private val viewSubTitleMargin = viewTitleMargin/2

    init {
        fitsSystemWindows = true
        add(appBarLayout(theme = R.style.AppTheme_AppBarOverlay) {
            add(toolbar {
                title = "自定义View整理"
                gravity = gravityCenter
                setTitleTextColor(TEXT_COLOR_PRIMARY)
                popupTheme = R.style.AppTheme_PopupOverlay
                (context as? AppCompatActivity)?.setSupportActionBar(this)
            }, defaultLParams())
        }, appBarLParams())
        add(scrollView {
            add(verticalLayout {
                /*addCustomView("基于SDK实现渲染~三角形",GLSurfaceView(requireActivity()).apply {
                    setRenderer(SimpleRender())
                })*/
                addCustomView("雷达波浪",::SimpleWaveView)
                addCustomView("开关按钮",::SwitchButtonView)
                addCustomView("庆祝礼赞",::CelebrateView).run {
                    onClick {
                        (it as CelebrateView).build()
                            .setPosition(-50f, width + 50f, -50f, -50f)
                            .streamFor(300,5000)
                    }
                }
                addCustomView("基于GLUES SDK实现渲染器",GLSurfaceView(requireActivity()).apply {
                    setRenderer(ColorRenderer())
                })
                addCustomView("基于NDK实现渲染器",GLSurfaceView(requireActivity()).apply {
                    setRenderer(NativeColorRenderer())
                })
                bottomPadding = dip(66)
            }, lParams(matchParent,wrapContent))
        }, contentScrollingWithAppBarLParams {
            margin = dip(16)
        })
    }

    private fun LinearLayout.addCustomView(viewName:String,createView: NewViewRef<View>) = run {
        val contentView:View
        add(textView {
            //textAppearance = MaterialR.style.TextAppearance_AppCompat_Headline
            textAppearance = MaterialR.style.TextAppearance_Material3_TitleMedium
            text = viewName
            centerText()
        },lParams(wrapContent,wrapContent,gravityCenterHorizontal))
        add(materialCardView {
            contentView = add(view(createView),lParams(matchParent,matchParent){
                margin = dip(viewSubTitleMargin)
            })
        },lParams(matchParent,dip(199)){
            margin = dip(viewTitleMargin)
        })
        contentView
    }
    private fun LinearLayout.addCustomView(viewName:String,createView: View) = run {
        val contentView:View
        add(textView {
            //textAppearance = MaterialR.style.TextAppearance_AppCompat_Headline
            textAppearance = MaterialR.style.TextAppearance_Material3_TitleMedium
            text = viewName
            centerText()
        },lParams(wrapContent,wrapContent,gravityCenterHorizontal))
        add(materialCardView {
            contentView = add(createView,lParams(matchParent,matchParent){
                margin = dip(viewSubTitleMargin)
            })
        },lParams(matchParent,dip(199)){
            margin = dip(viewTitleMargin)
        })
        contentView
    }
}