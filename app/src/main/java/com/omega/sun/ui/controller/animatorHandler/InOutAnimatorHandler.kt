package com.omega.sun.ui.controller.animatorHandler

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler

/**
 * Created by lengyacheng on 2023/2/9
 * desc
 */
class InOutAnimatorHandler: AnimatorChangeHandler() {
    override fun getAnimator(
        container: ViewGroup,
        from: View?,
        to: View?,
        isPush: Boolean,
        toAddedToContainer: Boolean
    ): Animator {

        val animator = AnimatorSet()
        val viewAnimators: MutableList<Animator> = ArrayList()

        if (isPush && to != null) {
            viewAnimators.add(
                ObjectAnimator.ofFloat(
                    to,
                    View.TRANSLATION_Y,
                    to.height.toFloat(),
                    0f
                )
            )
        } else if (!isPush && from != null) {
            viewAnimators.add(
                ObjectAnimator.ofFloat(
                    from,
                    View.TRANSLATION_Y,
                    from.height.toFloat()
                )
            )
        }

        animator.playTogether(viewAnimators)
        return animator

    }

    override fun resetFromView(from: View) {

    }
}
