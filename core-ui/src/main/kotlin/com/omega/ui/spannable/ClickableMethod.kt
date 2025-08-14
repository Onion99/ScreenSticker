package com.omega.ui.spannable

import android.text.NoCopySpan
import android.text.Selection
import android.text.Spannable
import android.text.method.ScrollingMovementMethod
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

/**
 * Created by lengyacheng on 2023/2/10
 * desc
 */
class ClickableMethod : ScrollingMovementMethod() {

    private val CLICK = 1
    private val UP = 2
    private val DOWN = 3

    override fun canSelectArbitrarily(): Boolean {
        return true
    }

    override fun handleMovementKey(
        widget: TextView, buffer: Spannable, keyCode: Int,
        movementMetaState: Int, event: KeyEvent
    ): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> if (KeyEvent.metaStateHasNoModifiers(
                    movementMetaState
                )
            ) {
                if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0 && action(
                        CLICK, widget, buffer
                    )
                ) {
                    return true
                }
            }
        }
        return super.handleMovementKey(widget, buffer, keyCode, movementMetaState, event)
    }

    override fun up(widget: TextView, buffer: Spannable): Boolean {
        return if (action(UP, widget, buffer)) {
            true
        } else super.up(widget, buffer)
    }

    override fun down(widget: TextView, buffer: Spannable): Boolean {
        return if (action(DOWN, widget, buffer)) {
            true
        } else super.down(widget, buffer)
    }

    override fun left(widget: TextView, buffer: Spannable): Boolean {
        return if (action(UP, widget, buffer)) {
            true
        } else super.left(widget, buffer)
    }

    override fun right(widget: TextView, buffer: Spannable): Boolean {
        return if (action(DOWN, widget, buffer)) {
            true
        } else super.right(widget, buffer)
    }

    private fun action(what: Int, widget: TextView, buffer: Spannable): Boolean {
        val layout = widget.layout
        val padding = widget.totalPaddingTop +
                widget.totalPaddingBottom
        val areaTop = widget.scrollY
        val areaBot = areaTop + widget.height - padding
        val lineTop = layout.getLineForVertical(areaTop)
        val lineBot = layout.getLineForVertical(areaBot)
        val first = layout.getLineStart(lineTop)
        val last = layout.getLineEnd(lineBot)
        val candidates = buffer.getSpans(
            first, last,
            ClickableSpan::class.java
        )
        val a = Selection.getSelectionStart(buffer)
        val b = Selection.getSelectionEnd(buffer)
        var selStart = Math.min(a, b)
        var selEnd = Math.max(a, b)
        if (selStart < 0) {
            if (buffer.getSpanStart(FROM_BELOW) >= 0) {
                selEnd = buffer.length
                selStart = selEnd
            }
        }
        if (selStart > last) {
            selEnd = Int.MAX_VALUE
            selStart = selEnd
        }
        if (selEnd < first) {
            selEnd = -1
            selStart = selEnd
        }
        var bestStart: Int
        var bestEnd: Int
        when (what) {
            CLICK -> {
                if (selStart == selEnd) {
                    return false
                }
                val links = buffer.getSpans(
                    selStart, selEnd,
                    ClickableSpan::class.java
                )
                if (links.size != 1) {
                    return false
                }
                val link = links[0]
            }
            UP -> {
                bestStart = -1
                bestEnd = -1
                var i = 0
                while (i < candidates.size) {
                    val end = buffer.getSpanEnd(candidates[i])
                    if (end < selEnd || selStart == selEnd) {
                        if (end > bestEnd) {
                            bestStart = buffer.getSpanStart(candidates[i])
                            bestEnd = end
                        }
                    }
                    i++
                }
                if (bestStart >= 0) {
                    Selection.setSelection(buffer, bestEnd, bestStart)
                    return true
                }
            }
            DOWN -> {
                bestStart = Int.MAX_VALUE
                bestEnd = Int.MAX_VALUE
                var i = 0
                while (i < candidates.size) {
                    val start = buffer.getSpanStart(candidates[i])
                    if (start > selStart || selStart == selEnd) {
                        if (start < bestStart) {
                            bestStart = start
                            bestEnd = buffer.getSpanEnd(candidates[i])
                        }
                    }
                    i++
                }
                if (bestEnd < Int.MAX_VALUE) {
                    Selection.setSelection(buffer, bestStart, bestEnd)
                    return true
                }
            }
        }
        return false
    }

    override fun onTouchEvent(
        widget: TextView, buffer: Spannable,
        event: MotionEvent
    ): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val links = buffer.getSpans(
                off, off,
                ClickableSpan::class.java
            )
            if (links.size != 0) {
                val link = links[0]
                if (action == MotionEvent.ACTION_UP) {
                    link.onClick(widget)
                }
                return true
            } else {
                Selection.removeSelection(buffer)
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    override fun initialize(widget: TextView?, text: Spannable) {
        Selection.removeSelection(text)
        text.removeSpan(FROM_BELOW)
    }

    override fun onTakeFocus(view: TextView?, text: Spannable, dir: Int) {
        Selection.removeSelection(text)
        if (dir and View.FOCUS_BACKWARD != 0) {
            text.setSpan(FROM_BELOW, 0, 0, Spannable.SPAN_POINT_POINT)
        } else {
            text.removeSpan(FROM_BELOW)
        }
    }

    companion object {
        private val FROM_BELOW: Any = NoCopySpan.Concrete()

        @JvmField
        val instance = ClickableMethod()
    }

}
