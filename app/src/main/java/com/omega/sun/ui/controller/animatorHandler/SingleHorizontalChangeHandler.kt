package com.omega.sun.ui.controller.animatorHandler

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler

open class SingleHorizontalChangeHandler : AnimatorChangeHandler {

    constructor() {}
    constructor(removesFromViewOnPush: Boolean) : super(removesFromViewOnPush) {}
    constructor(duration: Long) : super(duration) {}
    constructor(duration: Long, removesFromViewOnPush: Boolean) : super(duration, removesFromViewOnPush) {}

    override fun getAnimator(container: ViewGroup, from: View?, to: View?, isPush: Boolean, toAddedToContainer: Boolean): Animator {
        val animator = AnimatorSet()
        val viewAnimators: MutableList<Animator> = ArrayList()
        if (isPush && to != null) {
            viewAnimators.add(ObjectAnimator.ofFloat(to, View.TRANSLATION_X, to.width.toFloat(), 0f))
            from?.setWillNotDraw(true)
        } else if (!isPush && from != null) {
            viewAnimators.add(ObjectAnimator.ofFloat(from, View.TRANSLATION_X, from.width.toFloat()))
            to?.setWillNotDraw(false)
            animator.addListener(onStart = {
                val action = from.getTag(com.omega.resource.R.id.view_finish_action) as? Runnable
                action?.run()
            })
        }
        animator.playTogether(viewAnimators)
        return animator
    }

    override fun resetFromView(from: View) {}

    override fun copy(): ControllerChangeHandler {
        return SingleHorizontalChangeHandler(animationDuration, removesFromViewOnPush())
    }
}
