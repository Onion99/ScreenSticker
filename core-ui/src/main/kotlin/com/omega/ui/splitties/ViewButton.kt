package com.omega.ui.splitties

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.omega.resource.R
import splitties.experimental.InternalSplittiesApi
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.styles.styledView
import splitties.views.dsl.core.view
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun View.materialButtonFilled(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.materialButtonFilledWithIcon(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button_Icon,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.materialButtonFilledUnelevated(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button_UnelevatedButton,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.materialButtonFilledUnelevatedWithIcon(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button_UnelevatedButton_Icon,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.materialButtonOutlined(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button_OutlinedButton,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.materialButtonOutlinedWithIcon(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button_OutlinedButton_Icon,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.materialButtonText(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button_TextButton,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.materialButtonTextWithIcon(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button_TextButton_Icon,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.materialButtonTextDialog(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button_TextButton_Dialog,
        id = id,
        theme = theme,
        initView = initView
    )
}

inline fun View.materialButtonTextDialogWithIcon(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {}
): MaterialButton {
    return context.styledView(
        newViewRef = ::MaterialButton,
        styleAttr = R.attr.Widget_MaterialComponents_Button_TextButton_Dialog_Icon,
        id = id,
        theme = theme,
        initView = initView
    )
}

@OptIn(InternalSplittiesApi::class)
inline fun Context.floatingActionButton(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: FloatingActionButton.() -> Unit = {}
): FloatingActionButton {
    return view(id, theme, initView)
}

inline fun View.floatingActionButton(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: FloatingActionButton.() -> Unit = {}
): FloatingActionButton {
    return context.floatingActionButton(id, theme, initView)
}
