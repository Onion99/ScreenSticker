package com.omega.sun.ui.ext

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler
import com.omega.sun.ui.controller.animatorHandler.SingleBottomVerticalChangeHandler
import com.omega.sun.ui.controller.animatorHandler.SingleFadeChangeHandler
import com.omega.sun.ui.controller.animatorHandler.SingleHorizontalChangeHandler
import com.omega.sun.ui.controller.animatorHandler.SingleTopVerticalChangeHandler

const val CHANGE_HANDLE_DURATION = 299L
/**
 * Created by lengyacheng on 2023/2/10
 * desc
 */
val dialogPushChangeHandler = FadeChangeHandler(false)
val dialogPopChangeHandler = FadeChangeHandler(true)
val fadeChangeHandler = FadeChangeHandler(true)
val verticalChangeHandlerWithRemove = VerticalChangeHandler(true)
val verticalChangeHandler = VerticalChangeHandler(false)
val verticalTopChangeHandlerWithRemove  = SingleTopVerticalChangeHandler(true)
val verticalTopChangeHandler = SingleTopVerticalChangeHandler(false)
val horizontalChangeHandlerWithRemove = SingleHorizontalChangeHandler(CHANGE_HANDLE_DURATION,true)
val horizontalChangeHandler = SingleHorizontalChangeHandler(CHANGE_HANDLE_DURATION,false)
val singleFadeChangeHandlerWithRemove = SingleFadeChangeHandler(CHANGE_HANDLE_DURATION,true)
val singleFadeChangeHandler = SingleFadeChangeHandler(CHANGE_HANDLE_DURATION,false)

// ------------------------------------------------------------------------
// 跳转到新页面,不保存之前的View状�??
// ------------------------------------------------------------------------
fun Router.startNewPage(controller: Controller) {
    this.pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(fadeChangeHandler)
            .popChangeHandler(fadeChangeHandler)
    )
}


// ------------------------------------------------------------------------
// 跳转到新页面,保存之前的View状�??
// ------------------------------------------------------------------------
fun Router.startNewPageSaveStatusWithFade(controller: Controller) {
    this.pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(SingleFadeChangeHandler(CHANGE_HANDLE_DURATION,false))
            .popChangeHandler(SingleFadeChangeHandler(CHANGE_HANDLE_DURATION,true))
    )
}
fun Router.startNewPageSaveStatusWithFadeWithTag(controller: Controller) {
    this.pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(SingleFadeChangeHandler(CHANGE_HANDLE_DURATION,false))
            .popChangeHandler(SingleFadeChangeHandler(CHANGE_HANDLE_DURATION,true))
            .tag(controller::class.simpleName)
    )
}
fun Router.startNewPageSaveStatus(controller: Controller) {
    this.pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(SingleHorizontalChangeHandler(CHANGE_HANDLE_DURATION,false))
            .popChangeHandler(SingleHorizontalChangeHandler(CHANGE_HANDLE_DURATION,true))
    )
}

fun Router.startNewPageSaveStatusWithTag(controller: Controller) {
    this.pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(SingleHorizontalChangeHandler(CHANGE_HANDLE_DURATION,false))
            .popChangeHandler(SingleHorizontalChangeHandler(CHANGE_HANDLE_DURATION,true))
            .tag(controller::class.simpleName)
    )
}

// ------------------------------------------------------------------------
// 跳转到新页面-水平动画,保存之前的View状�??
// ------------------------------------------------------------------------
fun Router.startNewPageSaveStatusVerticalAnim(controller: Controller) {
    this.pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(SingleTopVerticalChangeHandler(false))
            .popChangeHandler(SingleTopVerticalChangeHandler(true))
    )
}
fun Router.startNewPageSaveStatusTopVerticalAnim(controller: Controller) {
    this.pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(SingleTopVerticalChangeHandler(false))
            .popChangeHandler(SingleTopVerticalChangeHandler(true))
    )
}
fun Router.startNewPageSaveStatusBottomVerticalAnim(controller: Controller) {
    this.pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(SingleBottomVerticalChangeHandler(false))
            .popChangeHandler(SingleBottomVerticalChangeHandler(true))
    )
}

// ------------------------------------------------------------------------
// �?出当前页�?
// ------------------------------------------------------------------------
fun Router.exit(controller: Controller) {
    this.popController(controller)
}

// ------------------------------------------------------------------------
// �?查当前Controller是否在队列中
// ------------------------------------------------------------------------
fun Router.checkControllerIsInRouter(tag:String):Boolean = kotlin.runCatching { getControllerWithTag(tag) != null }.getOrDefault(false)
// ------------------------------------------------------------------------
// �?查当前Controller是否在页�?
// ------------------------------------------------------------------------
fun Router.checkControllerIsInTop(controller:Controller):Boolean = backstack.isNotEmpty() && controller == backstack.last().controller
