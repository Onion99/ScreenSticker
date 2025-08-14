package com.omega.opengl.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.omega.opengl.helper.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// ------------------------------------------------------------------------
// 基于SDK实现渲染器-绘制圆点、直线和三角形
// ------------------------------------------------------------------------
class SimpleRender :GLSurfaceView.Renderer{
    private var program = 0

    // ---- 定义圆点坐标 ------
    private val vertexPoints = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )
    // ---- 色值 ------
    private val color = floatArrayOf(
        0.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f
    )
    // ---- 坐标占用内存,分配内存空间,每个浮点型占4字节空间 ------
    private val coordinateBuffer: FloatBuffer = ByteBuffer.allocateDirect(vertexPoints.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()

    // ---- 色值占用内存,分配内存空间,每个浮点型占4字节空间 ------
    private val colorBuffer: FloatBuffer = ByteBuffer.allocateDirect(color.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer();

    init {
        // ---- 传入坐标数据 ------
        coordinateBuffer.put(vertexPoints);
        coordinateBuffer.position(0);
        // ---- 传入色值数据 ------
        colorBuffer.put(color);
        colorBuffer.position(0);
    }

    // ------------------------------------------------------------------------
    // 顶点着色器
    // - 第一行表示：着色器的版本，OpenGL ES 2.0版本可以不写
    // - 第二行表示：输入属性的数组(一个名为vPosition的4分量向量)，layout (location = 0)表示这个变量的位置是顶点属性0
    // - 第五行表示：声明一个main函数
    // - 第六行表示：它将vPosition输入属性拷贝到名为gl_Position的特殊输出变量
    // - 第七行表示：它将浮点数据10.0拷贝到gl_PointSize的变量中
    // ------------------------------------------------------------------------
    private val coordinateShader = """
        #version 300 es 
        layout (location = 0) in vec4 vPosition;
        layout (location = 1) in vec4 aColor;
        out vec4 vColor;
        void main() { 
          gl_Position  = vPosition;
          gl_PointSize = 10.0;
          vColor = aColor;
        }
        """.trimIndent()

    // ------------------------------------------------------------------------
    // 顶点着色器
    // - 第一行表示：着色器的版本，OpenGL ES 2.0版本可以不写
    // - 第二行表示：声明着色器中浮点变量的默认精度
    // - 第三行表示：着色器声明一个输出变量fragColor，这个是一个4分量的向量
    // - 第六行表示：表示将颜色值(1.0,1.0,1.0,1.0)，输出到颜色缓冲区
    // ------------------------------------------------------------------------
    private val fragmentShader = """
        #version 300 es 
        precision mediump float;
        in vec4 vColor;
        out vec4 fragColor;
        void main() { 
          fragColor = vColor; 
        }
        """.trimIndent()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        //编译
        val vertexShaderId = ShaderUtils.compileVertexShader(coordinateShader)
        val fragmentShaderId = ShaderUtils.compileFragmentShader(fragmentShader)
        //鏈接程序片段
        program = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId);
        //在OpenGLES环境中使用程序片段
        GLES30.glUseProgram(program);
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 设置视图窗口
        GLES30.glViewport(0, 0, width, height);
    }

    override fun onDrawFrame(gl: GL10?) {
        // 把颜色缓冲区设置为我们预设的颜色
        GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT);
        //准备坐标数据
        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 0, coordinateBuffer);
        //启用顶点的句柄
        GLES30.glEnableVertexAttribArray(0);
        //绘制三角形颜色
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, 4, GLES30.GL_FLOAT, false, 0, colorBuffer);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
        //禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(0);
        GLES30.glDisableVertexAttribArray(1);
    }

}