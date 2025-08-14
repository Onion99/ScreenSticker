package com.omega.sun.ui.controller.animatorHandler

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler

class SingleFadeChangeHandler : AnimatorChangeHandler {

    constructor() {}
    constructor(removesFromViewOnPush: Boolean) : super(removesFromViewOnPush) {}
    constructor(duration: Long) : super(duration) {}
    constructor(duration: Long, removesFromViewOnPush: Boolean) : super(duration, removesFromViewOnPush) {}

    override fun getAnimator(container: ViewGroup, from: View?, to: View?, isPush: Boolean, toAddedToContainer: Boolean): Animator {
        val animator = AnimatorSet()
        val viewAnimators: MutableList<Animator> = ArrayList()
        if (isPush && to != null) {
            val start = if (toAddedToContainer) 0F else to.alpha
            viewAnimators.add(ObjectAnimator.ofFloat(to, View.ALPHA, start, 1f))
        } else if (!isPush && from != null) {
            viewAnimators.add(ObjectAnimator.ofFloat(from, View.ALPHA, 0f))
        }
        animator.playTogether(viewAnimators)
        return animator
    }

    override fun resetFromView(from: View) {}

    override fun copy(): ControllerChangeHandler {
        return SingleFadeChangeHandler(animationDuration, removesFromViewOnPush())
    }
}
