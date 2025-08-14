import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textfield.TextInputEditText
import com.omega.resource.R
import com.omega.ui.widget.text.AccurateWidthTextView
import com.omega.ui.widget.text.DefaultFontEditTextView
import com.omega.ui.widget.text.DefaultFontTextView
import splitties.experimental.InternalSplittiesApi
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.styles.styledView
import splitties.views.dsl.core.view
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import com.google.android.material.textfield.TextInputLayout
import com.omega.ui.splitties.styledView
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.wrapContent


@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.defaultFontTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: DefaultFontTextView.() -> Unit = {}
): DefaultFontTextView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::DefaultFontTextView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.defaultFontTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: DefaultFontTextView.() -> Unit = {}
): DefaultFontTextView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.defaultFontTextView(id, theme, initView)
}


@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.defaultFontEditTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: DefaultFontEditTextView.() -> Unit = {}
): DefaultFontEditTextView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::DefaultFontEditTextView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.defaultFontEditTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: DefaultFontEditTextView.() -> Unit = {}
): DefaultFontEditTextView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.defaultFontEditTextView(id, theme, initView)
}


@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.appCompatImageView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AppCompatImageView.() -> Unit = {}
): AppCompatImageView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::AppCompatImageView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.appCompatImageView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AppCompatImageView.() -> Unit = {}
): AppCompatImageView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.appCompatImageView(id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun Context.accurateWidthTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AccurateWidthTextView.() -> Unit = {}
): AccurateWidthTextView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return styledView(::AccurateWidthTextView,id, theme, initView)
}

@OptIn(ExperimentalContracts::class, InternalSplittiesApi::class)
inline fun View.accurateWidthTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AccurateWidthTextView.() -> Unit = {}
): AccurateWidthTextView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.accurateWidthTextView(id, theme, initView)
}


inline fun View.textInputLayoutFilledBox(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextInputLayout.() -> Unit = {}
): TextInputLayout {
    return context.styledView(
        newViewRef = ::TextInputLayout,
        styleAttr = R.attr.Widget_MaterialComponents_TextInputLayout_FilledBox,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.textInputLayoutFilledBoxDense(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextInputLayout.() -> Unit = {}
): TextInputLayout {
    return context.styledView(
        newViewRef = ::TextInputLayout,
        styleAttr = R.attr.Widget_MaterialComponents_TextInputLayout_FilledBox_Dense,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.textInputLayoutOutlinedBox(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextInputLayout.() -> Unit = {}
): TextInputLayout {
    return context.styledView(
        newViewRef = ::TextInputLayout,
        styleAttr = R.attr.Widget_MaterialComponents_TextInputLayout_OutlinedBox,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.textInputLayoutOutlinedBoxDense(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextInputLayout.() -> Unit = {}
): TextInputLayout {
    return context.styledView(
        newViewRef = ::TextInputLayout,
        styleAttr = R.attr.Widget_MaterialComponents_TextInputLayout_OutlinedBox_Dense,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun TextInputLayout.addInput(
    @IdRes id: Int = View.NO_ID,
    initView: TextInputEditText.() -> Unit = {}
): TextInputEditText {
    return add(
        view(
            createView = ::TextInputEditText,
            id = id,
            initView = initView
        ), LinearLayout.LayoutParams(matchParent, wrapContent)
    )
}
