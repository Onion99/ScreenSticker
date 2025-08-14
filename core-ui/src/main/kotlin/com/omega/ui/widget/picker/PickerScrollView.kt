package com.omega.ui.widget.picker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.omega.resource.R
import splitties.dimensions.dip
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

/**
 * 滚动选择�?
 */
class PickerScrollView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : View(context, attrs){


    private var mDataList: MutableList<PickerBean> = mutableListOf()
    // 选中的位置，这个位置是mDataList的中心位置，�?直不�?
    private var mCurrentSelected = 0
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var whitePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mMaxTextSize = 14f
    private var mMinTextSize = 12f
    private val mMaxTextAlpha = 255f
    private val mMinTextAlpha = 120f
    private var mViewHeight = 0
    private var mViewWidth = 0
    private var mLastDownY = 0f
    // text之间间距和minTextSize之比
    private val marginAlpha = 2.8f
    // 自动回滚到中间的速度
    val speed = 2f
    // 滑动的距�?
    private var mMoveLen = 0f
    private var isInit = false
    private var mSelectListener: OnSelectListener? = null
    private var timer = Timer()
    private var updateHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (abs(mMoveLen) < speed) {
                mMoveLen = 0f
                mTask.cancel()
                performSelect()
            } else mMoveLen -= mMoveLen / abs(mMoveLen) * speed // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下�?
            invalidate()
        }
    }
    private var mTask = MyTimerTask(updateHandler)
    var typeName = ""
    var textSize = dip(16f)


    init {
//        val customTypeface = ResourcesCompat.getFont(context!!, R.font.metropolis_semibold)
//        mPaint.typeface = customTypeface
        mPaint.style = Paint.Style.FILL
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.color = Color.parseColor("#222222")
        whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
    }

    fun setOnSelectListener(listener: OnSelectListener?) {
        mSelectListener = listener
    }

    private fun performSelect() {
        if (mSelectListener != null) mSelectListener?.onSelect(mDataList[mCurrentSelected])
    }

    fun setData(pickerList: MutableList<PickerBean>) {
        mDataList = pickerList
        mCurrentSelected = pickerList.size / 2
        invalidate()
    }

    fun setSelected(selected: Int) {
        mCurrentSelected = initCurrentSelect(selected)
        invalidate()
    }

    private fun initCurrentSelect(selected: Int): Int {
        for (i in mDataList.indices) {
            if (mDataList[i].pickerName == selected) {
                return i
            }
        }
        return mDataList.size / 4
    }



    private fun moveHeadToTail() {
        val bean = mDataList[0]
        mDataList.removeAt(0)
        mDataList.add(bean)
    }

    private fun moveTailToHead() {
        val bean = mDataList[mDataList.size - 1]
        mDataList.removeAt(mDataList.size - 1)
        mDataList.add(0, bean)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewHeight = measuredHeight
        mViewWidth = measuredWidth
        // 按照View的高度计算字体大�?
        mMaxTextSize = mViewHeight / 7.0f
        mMinTextSize = mMaxTextSize / 2f
        isInit = true
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // draw bg
        val baseline = measuredHeight/2
        canvas.drawRoundRect(
            dip(21f),
            baseline - dip(20f),
            measuredWidth - dip(10f),
            baseline + dip(20f),
            dip(10f),// corner radius
            dip(10f),// corner radius
            whitePaint
        )
        // 根据index绘制view
        if (isInit) drawData(canvas)

    }

    private fun drawData(canvas: Canvas) {
        // 先绘制�?�中的text再往上往下绘制其余的text
        val scale = parabola(mViewHeight / 4.0f, mMoveLen)
        mPaint.textSize = dip(16f)
        mPaint.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()

        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        val x = (mViewWidth / 2.0).toFloat()
        val y = (mViewHeight / 2.0 + mMoveLen).toFloat()
        val fmi = mPaint.fontMetricsInt
        val baseline = (y - (fmi.bottom / 2.0 + fmi.top / 2.0)).toFloat()
        val index = mCurrentSelected
        val textData = if(mDataList.isEmpty()) "$typeName" else "${mDataList[index].pickerName}$typeName"
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // last out
        paint.color = Color.WHITE
        mPaint.color = ContextCompat.getColor(context, R.color.text_color_primary)
        canvas.drawText(textData, x, baseline, mPaint)
        // center out
        mPaint.color = Color.parseColor("#AAAAAA")
        // 绘制上方data
        run {
            var i = 1
            while (mCurrentSelected - i >= 0) {
                drawOtherText(canvas, i, -1)
                i++
            }
        }
        // 绘制下方data
        var i = 1
        while (mCurrentSelected + i < mDataList.size) {
            drawOtherText(canvas, i, 1)
            i++
        }
    }

    /**
     * @param position 距离mCurrentSelected的差�?
     * @param type     1表示向下绘制�?-1表示向上绘制
     */
    private fun drawOtherText(canvas: Canvas, position: Int, type: Int) {
        val d = (marginAlpha * mMinTextSize * position + type * mMoveLen)
        val scale = parabola(mViewHeight / 4.0f, d)
        mPaint.textSize = textSize
        mPaint.alpha = ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha).toInt()
        val y = (mViewHeight / 2.0 + type * d).toFloat()
        val fmi = mPaint.fontMetricsInt
        val baseline = (y - (fmi.bottom / 2.0 + fmi.top / 2.0)).toFloat()
        val index = mCurrentSelected + type * position
        val textData = "${mDataList[index].pickerName}$typeName"
        canvas.drawText(textData, (mViewWidth / 2.0).toFloat(), baseline, mPaint)
    }

    /**
     * 抛物�?
     *
     * @param zero 零点坐标
     * @param x    偏移�?
     * @return scale
     */
    private fun parabola(zero: Float, x: Float): Float {
        val f = (1 - (x / zero).toDouble().pow(2.0)).toFloat()
        return if (f < 0) 0F else f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> doDown(event)
            MotionEvent.ACTION_MOVE -> doMove(event)
            MotionEvent.ACTION_UP -> doUp(event)
        }
        return true
    }

    private fun doDown(event: MotionEvent) {
        mTask.cancel()
        mLastDownY = event.y
    }

    private fun doMove(event: MotionEvent) {
        mMoveLen += event.y - mLastDownY
        if (mMoveLen > marginAlpha * mMinTextSize / 2) {
            // �?下滑超过离开距离
            moveTailToHead()
            mMoveLen -= marginAlpha * mMinTextSize
        } else if (mMoveLen < -marginAlpha * mMinTextSize / 2) {
            // �?上滑超过离开距离
            moveHeadToTail()
            mMoveLen += marginAlpha * mMinTextSize
        }
        mLastDownY = event.y
        invalidate()
    }

    private fun doUp(event: MotionEvent) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间�?�中位置
        if (abs(mMoveLen) < 0.0001) {
            mMoveLen = 0f
            return
        }
        mTask.cancel()
        mTask = MyTimerTask(updateHandler)
        timer.schedule(mTask, 0, 10)
    }

    internal class MyTimerTask(private var handler: Handler) : TimerTask() {
        override fun run() {
            handler.sendMessage(handler.obtainMessage())
        }
    }

    interface OnSelectListener {
        fun onSelect(pickers: PickerBean?)
    }

    private fun dp2Px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density //当前屏幕密度因子
        return (dp * scale + 0.5f).toInt()
    }
}
