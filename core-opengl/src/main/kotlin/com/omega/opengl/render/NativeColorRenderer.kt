package com.omega.opengl.render

import android.graphics.Color
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// ------------------------------------------------------------------------
// 基于NDK实现渲染器  - 绘制纯色背景
// ------------------------------------------------------------------------
class NativeColorRenderer : GLSurfaceView.Renderer{

    private var customColor = Color.BLUE

    init {
        System.loadLibrary("native-color");
    }

    external fun surfaceCreated(color: Int)

    external fun surfaceChanged(width: Int, height: Int)

    external fun onDrawFrame()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景颜色
        val redF = Color.red(customColor) / 255F
        val greenF = Color.green(customColor) / 255F
        val blueF = Color.blue(customColor) / 255F
        val alphaF = Color.alpha(customColor) / 255F
        GLES30.glClearColor(redF, greenF, blueF, alphaF)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 设置视图窗口
        GLES30.glViewport(0, 0, width, height);
    }

    override fun onDrawFrame(gl: GL10?) {
        // 把颜色缓冲区设置为我们预设的颜色
        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

}