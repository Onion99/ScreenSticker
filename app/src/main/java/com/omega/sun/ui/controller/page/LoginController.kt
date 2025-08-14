package com.omega.sun.ui.controller.page

import addInput
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import com.omega.sun.ui.controller.base.BaseLifecycleController
import com.omega.ui.splitties.floatingActionButton
import com.omega.ui.splitties.materialButtonFilled
import com.omega.ui.splitties.onClick
import splitties.dimensions.dip
import splitties.views.InputType
import splitties.views.dsl.core.add
import splitties.views.dsl.core.endMargin
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.verticalLayout
import splitties.views.dsl.core.wrapContent
import splitties.views.gravityBottomEnd
import splitties.views.gravityCenter
import splitties.views.gravityCenterHorizontal
import splitties.views.horizontalPadding
import splitties.views.imageResource
import splitties.views.type
import textInputLayoutOutlinedBox

class LoginController:BaseLifecycleController() {
    override fun onCreateView(context: Context): View  = context.frameLayout {
        val mobileInput = textInputLayoutOutlinedBox {
            addInput{
                hint = "手机号码"
                type = InputType.number
            }
        }

        val verificationInput = textInputLayoutOutlinedBox {
            addInput{
                hint = "验证码"
                type = InputType.number
            }
        }

        add(verticalLayout {
            horizontalPadding = dip(29)
            add(mobileInput,lParams(matchParent,wrapContent))
            add(verificationInput,lParams(matchParent,wrapContent){
                topMargin = dip(10)
            })
            add(materialButtonFilled{
                text = "获取验证码"
                cornerRadius = dip(26)
                onClick {}
            },lParams(wrapContent,wrapContent,gravityCenterHorizontal){
                topMargin = dip(10)
            })
        },lParams(matchParent,wrapContent,gravityCenter))

        add(floatingActionButton {
            imageResource = android.R.drawable.ic_menu_view
            imageTintList  = ColorStateList.valueOf(Color.WHITE)
            rotation = 180f
            onClick {
            }
        },lParams(wrapContent,wrapContent,gravityBottomEnd){
            bottomMargin = dip(20)
            endMargin = dip(20)
        })
    }
}