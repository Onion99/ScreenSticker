package com.omega.ui.splitties

import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import splitties.views.isConfigRtl

fun View.rotationForever(clockwise: Boolean = !isConfigRtl, duration: Long = 800L) {
    if (isVisible.not()) return
    animate()
        .rotationBy(if (clockwise) 360F else -360F)
        .setDuration(duration)
        .setInterpolator(LinearInterpolator())
        .withEndAction {
            rotationForever(clockwise, duration)
        }
        .start()
}
fun View.alphaForever(clockwise: Boolean = true, duration: Long = 800L) {
    if (isVisible.not()) return
    animate()
        .alpha(if (clockwise) 1F else 0F)
        .setDuration(duration)
        .setInterpolator(LinearInterpolator())
        .withEndAction {
            alphaForever(!clockwise, duration)
        }
        .start()
}
