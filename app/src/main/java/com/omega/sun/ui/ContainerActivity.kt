package com.omega.sun.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraXConfig
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.omega.resource.R
import com.omega.sun.ui.controller.page.HomeController
import com.omega.sun.ui.controller.page.LoginController
import com.omega.ui.splitties.changeHandlerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

@AndroidEntryPoint
class ContainerActivity : AppCompatActivity(),CoroutineScope by MainScope(){

    lateinit var router: Router


    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreate(savedInstanceState: Bundle?) {
        // ---- 设置Splash Screen开屏 处理，必须onCreate前 ------
        installSplashScreen()
        theme.applyStyle(R.style.MaterialComponentsStyles,false)
        super.onCreate(savedInstanceState)
        // ---- router handle,必须放在 super.onCreate 后面,避免初始化ViewModel 崩溃 ------
        val changeHandlerFrameLayout = changeHandlerFrameLayout(R.id.fragment_container_view)
        // ---- set up page ------
        router = Conductor.attachRouter(this, changeHandlerFrameLayout, null).setPopRootControllerMode(Router.PopRootControllerMode.NEVER)
        router.setRoot(RouterTransaction.with(HomeController()))
        // ---- Init View ------
        setContentView(changeHandlerFrameLayout)
    }





    private var lastClickExitTime:Long = 0L
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!router.handleBack()) {
            if(lastClickExitTime + 1000 > SystemClock.uptimeMillis()){
                super.onBackPressed()
            } else lastClickExitTime  = SystemClock.uptimeMillis()
        }
    }
}
