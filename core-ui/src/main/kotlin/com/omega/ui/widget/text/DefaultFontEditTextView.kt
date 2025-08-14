package com.omega.ui.widget.text

import android.graphics.Typeface
import android.text.InputFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText


// ------------------------------------------------------------------------
// 默认字体EditTextView
// ------------------------------------------------------------------------
class DefaultFontEditTextView @JvmOverloads constructor(
    context: android.content.Context,
    attrs: AttributeSet? = null,
) : AppCompatEditText(context, attrs) {

    fun setMaxLength(maxLength: Int) {
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }
}
