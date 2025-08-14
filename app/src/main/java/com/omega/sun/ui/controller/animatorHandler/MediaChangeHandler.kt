package com.omega.sun.ui.controller.animatorHandler

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup


class MediaChangeHandler : SingleHorizontalChangeHandler {

    constructor() {}

    constructor(removesFromViewOnPush: Boolean) : super(removesFromViewOnPush) {}

    var switch = false

    override fun getAnimator(
        container: ViewGroup,
        from: View?,
        to: View?,
        isPush: Boolean,
        toAddedToContainer: Boolean
    ): Animator {
        if (switch) {
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
        return super.getAnimator(container, from, to, isPush, toAddedToContainer)
    }
}
