package com.omega.ui.drawabletoolbox

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.RotateDrawable
import android.os.Build
import android.util.Log
import java.lang.reflect.Field
import java.lang.reflect.Method

private val gradientState = resolveGradientState()

private fun resolveGradientState(): Class<*> {
    val classes = GradientDrawable::class.java.declaredClasses
    for (singleClass in classes) {
        if (singleClass.simpleName == "GradientState") return singleClass
    }
    throw RuntimeException("GradientState could not be found in current GradientDrawable implementation")
}

private val rotateState = resolveRotateState()

private fun resolveRotateState(): Class<*> {
    val classes = RotateDrawable::class.java.declaredClasses
    for (singleClass in classes) {
        if (singleClass.simpleName == "RotateState") return singleClass
    }
    throw RuntimeException("RotateState could not be found in current RotateDrawable implementation")
}

@Throws(SecurityException::class, NoSuchFieldException::class)
private fun resolveField(source: Class<*>, fieldName: String): Field {
    val field = source.getDeclaredField(fieldName)
    field.isAccessible = true
    return field
}

@Throws(SecurityException::class, NoSuchMethodException::class)
private fun resolveMethod(source: Class<*>, methodName: String, vararg parameterTypes: Class<*>): Method {
    val method = source.getDeclaredMethod(methodName, *parameterTypes)
    method.isAccessible = true
    return method
}

fun setInnerRadius(drawable: GradientDrawable, value: Int) {
    try {
        val innerRadius = resolveField(gradientState, "mInnerRadius")
        innerRadius.setInt(drawable.constantState, value)
    } catch (e: NoSuchFieldException) {
        Log.d("Compatible", "setInnerRadius: ${e.message}")
    } catch (e: IllegalAccessException) {
        Log.d("Compatible", "setInnerRadius: ${e.message}")
    }
}

fun setInnerRadiusRatio(drawable: GradientDrawable, value: Float) {
    try {
        val innerRadius = resolveField(gradientState, "mInnerRadiusRatio")
        innerRadius.setFloat(drawable.constantState, value)
    } catch (e: NoSuchFieldException) {
        Log.d("Compatible", "setInnerRadiusRatio: ${e.message}")
    } catch (e: IllegalAccessException) {
        Log.d("Compatible", "setInnerRadiusRatio: ${e.message}")
    }
}

fun setThickness(drawable: GradientDrawable, value: Int) {
    try {
        val innerRadius = resolveField(gradientState, "mThickness")
        innerRadius.setInt(drawable.constantState, value)
    } catch (e: NoSuchFieldException) {
        Log.d("Compatible", "setThickness: ${e.message}")
    } catch (e: IllegalAccessException) {
        Log.d("Compatible", "setThickness: ${e.message}")
    }
}

fun setThicknessRatio(drawable: GradientDrawable, value: Float) {
    try {
        val innerRadius = resolveField(gradientState, "mThicknessRatio")
        innerRadius.setFloat(drawable.constantState, value)
    } catch (e: NoSuchFieldException) {
        Log.d("Compatible", "setThicknessRatio: ${e.message}")
    } catch (e: IllegalAccessException) {
        Log.d("Compatible", "setThicknessRatio: ${e.message}")
    }
}

fun setUseLevelForShape(drawable: GradientDrawable, value: Boolean) {
    try {
        val useLevelForShape = resolveField(gradientState, "mUseLevelForShape")
        useLevelForShape.setBoolean(drawable.constantState, value)
    } catch (e: NoSuchFieldException) {
        Log.d("Compatible", "setUseLevelForShape: ${e.message}")
    } catch (e: IllegalAccessException) {
        Log.d("Compatible", "setUseLevelForShape: ${e.message}")
    }
}

fun setOrientation(drawable: GradientDrawable, value: GradientDrawable.Orientation) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        drawable.orientation = value
    } else {
        try {
            val orientation = resolveField(gradientState, "mOrientation")
            orientation.set(drawable.constantState, value)
            val rectIdDirty = resolveField(GradientDrawable::class.java, "mRectIsDirty")
            rectIdDirty.setBoolean(drawable, true)
            drawable.invalidateSelf()
        } catch (e: NoSuchFieldException) {
            Log.d("Compatible", "setOrientation: ${e.message}")
        } catch (e: IllegalAccessException) {
            Log.d("Compatible", "setOrientation: ${e.message}")
        }
    }
}

fun setColors(drawable: GradientDrawable, value: IntArray) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        drawable.colors = value
    } else {
        try {
            val colors = resolveField(gradientState, "mColors")
            colors.set(drawable.constantState, value)
            drawable.invalidateSelf()
        } catch (e: NoSuchFieldException) {
            Log.d("Compatible", "setColors: ${e.message}")
        } catch (e: IllegalAccessException) {
            Log.d("Compatible", "setColors: ${e.message}")
        }
    }
}

fun setGradientRadiusType(drawable: GradientDrawable, value: Int) {
    try {
        val type = resolveField(gradientState, "mGradientRadiusType")
        type.setInt(drawable.constantState, value)
    } catch (e: NoSuchFieldException) {
        Log.d("Compatible", "setGradientRadiusType: ${e.message}")
    } catch (e: IllegalAccessException) {
        Log.d("Compatible", "setGradientRadiusType: ${e.message}")
    }
}

fun setGradientRadius(drawable: GradientDrawable, value: Float) {
    try {
        val gradientRadius = resolveField(gradientState, "mGradientRadius")
        gradientRadius.setFloat(drawable.constantState, value)
    } catch (e: NoSuchFieldException) {
        Log.d("Compatible", "setGradientRadius: ${e.message}")
    } catch (e: IllegalAccessException) {
        Log.d("Compatible", "setGradientRadius: ${e.message}")
    }
}

fun setStrokeColor(drawable: GradientDrawable, value: Int) {
    try {
        val type = resolveField(gradientState, "mStrokeColor")
        type.setInt(drawable.constantState, value)
    } catch (e: NoSuchFieldException) {
        Log.d("Compatible", "setStrokeColor: ${e.message}")
    } catch (e: IllegalAccessException) {
        Log.d("Compatible", "setStrokeColor: ${e.message}")
    }
}

fun setDrawable(rotateDrawable: RotateDrawable, drawable: Drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        rotateDrawable.drawable = drawable
    } else {
        try {
            val drawableField = resolveField(rotateState, "mDrawable")
            val stateField = resolveField(RotateDrawable::class.java, "mState")
            drawableField.set(stateField.get(rotateDrawable), drawable)
            drawable.callback = rotateDrawable
        } catch (e: NoSuchFieldException) {
            Log.d("Compatible", "setDrawable: ${e.message}")
        } catch (e: IllegalAccessException) {
            Log.d("Compatible", "setDrawable: ${e.message}")
        }
    }
}

fun setPivotX(rotateDrawable: RotateDrawable, value: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        rotateDrawable.pivotX = value
    } else {
        try {
            val pivotXField = resolveField(rotateState, "mPivotX")
            pivotXField.setFloat(rotateDrawable.constantState, value)
            val pivotXRelField = resolveField(rotateState, "mPivotXRel")
            pivotXRelField.setBoolean(rotateDrawable.constantState, true)
        } catch (e: NoSuchFieldException) {
            Log.d("Compatible", "setPivotX: ${e.message}")
        } catch (e: IllegalAccessException) {
            Log.d("Compatible", "setPivotX: ${e.message}")
        }
    }
}

fun setPivotY(rotateDrawable: RotateDrawable, value: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        rotateDrawable.pivotY = value
    } else {
        try {
            val pivotYField = resolveField(rotateState, "mPivotY")
            pivotYField.setFloat(rotateDrawable.constantState, value)
            val pivotYRelField = resolveField(rotateState, "mPivotYRel")
            pivotYRelField.setBoolean(rotateDrawable.constantState, true)
        } catch (e: NoSuchFieldException) {
            Log.d("Compatible", "setPivotY: ${e.message}")
        } catch (e: IllegalAccessException) {
            Log.d("Compatible", "setPivotY: ${e.message}")
        }
    }
}

fun setFromDegrees(rotateDrawable: RotateDrawable, value: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        rotateDrawable.fromDegrees = value
    } else {
        try {
            val fromDegreesField = resolveField(rotateState, "mFromDegrees")
            fromDegreesField.setFloat(rotateDrawable.constantState, value)
        } catch (e: NoSuchFieldException) {
            Log.d("Compatible", "setFromDegrees: ${e.message}")
        } catch (e: IllegalAccessException) {
            Log.d("Compatible", "setFromDegrees: ${e.message}")
        }
    }
}

fun setToDegrees(rotateDrawable: RotateDrawable, value: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        rotateDrawable.toDegrees = value
    } else {
        try {
            val toDegreesField = resolveField(rotateState, "mToDegrees")
            toDegreesField.setFloat(rotateDrawable.constantState, value)
        } catch (e: NoSuchFieldException) {
            Log.d("Compatible", "setToDegrees: ${e.message}")
        } catch (e: IllegalAccessException) {
            Log.d("Compatible", "setToDegrees: ${e.message}")
        }
    }
}

fun setRadius(rippleDrawable: RippleDrawable, value: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        rippleDrawable.radius = value
    } else {
        try {
            val setRadiusMethod = resolveMethod(RippleDrawable::class.java, "setMaxRadius", Int::class.java)
            setRadiusMethod.invoke(rippleDrawable, value)
        } catch (e: NoSuchFieldException) {
            Log.d("Compatible", "setRadius: ${e.message}")
        } catch (e: IllegalAccessException) {
            Log.d("Compatible", "setRadius: ${e.message}")
        }
    }
}
