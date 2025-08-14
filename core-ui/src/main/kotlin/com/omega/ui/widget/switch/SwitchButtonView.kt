package com.omega.ui.widget.switch

import android.R
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AccelerateInterpolator
import android.widget.CompoundButton
import androidx.core.graphics.ColorUtils
import com.omega.ui.util.ColorUtil
import kotlin.math.max
import kotlin.math.min


private var CHECKED_PRESSED_STATE: IntArray = intArrayOf(R.attr.state_checked, R.attr.state_enabled, R.attr.state_pressed)
private var UNCHECKED_PRESSED_STATE: IntArray = intArrayOf(-R.attr.state_checked, R.attr.state_enabled, R.attr.state_pressed)
private const val  DEFAULT_THUMB_SIZE_DP = 126
private const val  DEFAULT_TINT_COLOR = 0x327FC2
private const val  DEFAULT_BACK_MEASURE_RATIO = 1.8f;

class SwitchButtonView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CompoundButton(context, attrs,defStyleAttr){

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // ---- 底层View背景范围 ------
    private val backGroundRectF = RectF()
    private val safeOffsetRectF = RectF()
    // ---- 焦点按钮背景范围 ------
    private val thumbBackgroundRectF = RectF()
    // ---- 焦点按钮大小 ------
    private val thumbButtonPointF = PointF()
    // ---- 焦点按钮间距 ------
    private val thumbButtonMargin = RectF()
    // ---- 焦点按钮范围 ------
    private val presentThumbButtonRectF = RectF()

    // ---- 底层背景测量比例 ------
    private var mBackMeasureRatio = 0f
    // ---- 当前处理状态 0 -关闭 1-开启 ------
    private var processState = 0f
        set(value) {
            // 做最大限制,免得焦点跑出范围
            field = value.coerceAtMost(1f).coerceAtLeast(0f)
            invalidate()
        }



    private var currentThumbButtonColor = 0xff4751
    private var currentBackgroundColor = 0xff58aa
    // ---- 背景颜色 ------
    private var backgroundColor = ColorStateList.valueOf(currentBackgroundColor)
    // ---- 焦点按钮颜色 ------
    private var thumbButtonColor= ColorStateList.valueOf(currentThumbButtonColor)
    // ---- 下一次状态背景颜色 ------
    private var nextBackgroundColor = 0
    // ---- 焦点按钮圆角值 ------
    private var thumbButtonRadius = 0f
    // ---- 焦点按钮背景圆角值 ------
    private var backgroundRadius = 0f

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var measuredWidth: Int

        var minWidth = (thumbButtonPointF.x * mBackMeasureRatio).toInt()
        minWidth = max(minWidth, (minWidth + thumbButtonMargin.left + thumbButtonMargin.right).toInt())
        minWidth = max(minWidth, minWidth + paddingLeft + paddingRight)
        minWidth = max(minWidth, suggestedMinimumWidth)

        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = max(minWidth, widthSize)
        } else {
            measuredWidth = minWidth
            if (widthMode == MeasureSpec.AT_MOST)
                measuredWidth = min(measuredWidth, widthSize)
        }

        return measuredWidth
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var measuredHeight: Int

        var minHeight = max(thumbButtonPointF.y, thumbButtonPointF.y + thumbButtonMargin.top + thumbButtonMargin.right).toInt()

        minHeight = max(minHeight, suggestedMinimumHeight)
        minHeight = max(minHeight, minHeight + paddingTop + paddingBottom)

        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = max(minHeight, heightSize)
        } else {
            measuredHeight = minHeight
            if (heightMode == MeasureSpec.AT_MOST)
                measuredHeight = min(measuredHeight, heightSize)
        }

        return measuredHeight
    }

    // ------------------------------------------------------------------------
    // 绘制范围初始化
    // ------------------------------------------------------------------------
    private fun setup() {
        val thumbTop = paddingTop + max(0f, thumbButtonMargin.top)
        val thumbLeft = paddingLeft + max(0f, thumbButtonMargin.left)

        thumbBackgroundRectF.set(thumbLeft, thumbTop, thumbLeft + thumbButtonPointF.x,thumbTop + thumbButtonPointF.y)

        val backLeft = thumbBackgroundRectF.left - thumbButtonMargin.left
        backGroundRectF.set(backLeft, thumbBackgroundRectF.top - thumbButtonMargin.top, backLeft + thumbButtonMargin.left + thumbButtonPointF.x * mBackMeasureRatio + thumbButtonMargin.right, thumbBackgroundRectF.bottom + thumbButtonMargin.bottom)
        safeOffsetRectF.set(thumbBackgroundRectF.left, 0f, backGroundRectF.right - thumbButtonMargin.right - thumbBackgroundRectF.width(), 0f)

        val minBackRadius = min(backGroundRectF.width(), backGroundRectF.height()) / 2f
        backgroundRadius = min(minBackRadius, backgroundRadius)
    }

    private var processAnimator:ObjectAnimator? = null

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        if(processAnimator == null){
            processAnimator = ObjectAnimator.ofFloat(this, "processState", 0f, 0f).apply {
                interpolator = AccelerateInterpolator()
                duration = 500
            }
        }
        if(processAnimator?.isRunning == true) processAnimator!!.cancel()
        if (checked) {
            processAnimator?.setFloatValues(processState, 1f);
        } else {
            processAnimator?.setFloatValues(processState, 0f);
        }
        processAnimator?.start()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        currentThumbButtonColor = thumbButtonColor.getColorForState(drawableState, currentThumbButtonColor);
        val nextState = if (isChecked) UNCHECKED_PRESSED_STATE else CHECKED_PRESSED_STATE
        currentBackgroundColor = backgroundColor.getColorForState(drawableState, currentBackgroundColor)
        nextBackgroundColor = backgroundColor.getColorForState(nextState, currentBackgroundColor)
    }

    private var touchStartX = 0f
    private var touchStartY = 0f
    private var touchLastX  = 0f
    private val touchSlop by lazy(LazyThreadSafetyMode.NONE) {
        ViewConfiguration.get(context).scaledTouchSlop
    }
    private val clickTimeout by lazy(LazyThreadSafetyMode.NONE) {
        ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout()
    }

    // ------------------------------------------------------------------------
    // 触摸事件处理
    // ------------------------------------------------------------------------
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        val action = event.action
        val deltaX = event.x - touchStartX
        val deltaY = event.y - touchStartY
        var nextStatus = false
        when(action){
            MotionEvent.ACTION_DOWN -> {
                // ---- 防止触摸事件被父View拦截 ------
                parent?.requestDisallowInterceptTouchEvent(false)
                touchStartX = event.x
                touchStartY = event.y
                touchLastX = touchStartX
                isPressed = true
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                processState = (processState + (x - touchLastX) / safeOffsetRectF.width())
                touchLastX = x
            }
            MotionEvent.ACTION_CANCEL,MotionEvent.ACTION_UP -> {
                isPressed = false
                nextStatus = processState > 0.5f
                val countTime = event.eventTime - event.downTime
                if(deltaX < touchSlop && deltaY < touchSlop && countTime < clickTimeout){
                    performClick()
                }else {
                    isChecked = nextStatus
                }
            }
        }
        return true
    }

    // ------------------------------------------------------------------------
    // prepare data
    // ------------------------------------------------------------------------
    init {
        val density = resources.displayMetrics.density
        val thumbWidth  = density * DEFAULT_THUMB_SIZE_DP
        val thumbHeight = density * DEFAULT_THUMB_SIZE_DP
        val backMeasureRatio  = DEFAULT_BACK_MEASURE_RATIO

        thumbButtonColor = ColorUtil.generateThumbColorWithTintColor(DEFAULT_TINT_COLOR)
        currentThumbButtonColor = thumbButtonColor.defaultColor
        thumbButtonPointF.set(thumbWidth, thumbHeight);

        backgroundColor = ColorUtil.generateBackColorWithTintColor(DEFAULT_TINT_COLOR)
        currentBackgroundColor = backgroundColor.defaultColor
        nextBackgroundColor = backgroundColor.getColorForState(CHECKED_PRESSED_STATE, currentBackgroundColor)

        thumbButtonMargin.set(12f,12f,12f,12f)

        // size & measure params must larger than 1
        mBackMeasureRatio = if (thumbButtonMargin.width() >= 0) backMeasureRatio.coerceAtLeast(1f) else backMeasureRatio

        thumbButtonRadius = density * DEFAULT_THUMB_SIZE_DP / 2
        backgroundRadius = thumbButtonRadius

        isFocusable = true
        isClickable = true
        if(isChecked){
            processState = 1f
        }
    }

    // ------------------------------------------------------------------------
    // location
    // ------------------------------------------------------------------------
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            setup();
        }
    }

    // ------------------------------------------------------------------------
    // draw
    // ------------------------------------------------------------------------
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var alpha = (255 * (if (isChecked) processState else (1 - processState)).toInt())
        // ----第一层背景-绘制------
        var colorAlpha: Int = Color.alpha(currentBackgroundColor)
        colorAlpha = colorAlpha * alpha / 255
        paint.setColor(ColorUtils.setAlphaComponent(currentBackgroundColor,colorAlpha))
        canvas.drawRoundRect(backGroundRectF, backgroundRadius, backgroundRadius, paint)


        // ----第二层动画过程背景-绘制-------
        alpha = 255 - alpha
        colorAlpha = Color.alpha(nextBackgroundColor)
        colorAlpha = colorAlpha * alpha / 255
        paint.setColor(ColorUtils.setAlphaComponent(nextBackgroundColor,colorAlpha))
        canvas.drawRoundRect(backGroundRectF, backgroundRadius, backgroundRadius, paint)

        // ----状态圆点-绘制 ------
        paint.alpha = 255
        presentThumbButtonRectF.set(thumbBackgroundRectF)
        presentThumbButtonRectF.offset(processState * safeOffsetRectF.width(),0f)
        paint.setColor(currentThumbButtonColor)
        canvas.drawRoundRect(presentThumbButtonRectF,thumbButtonRadius,thumbButtonRadius,paint)
    }
}