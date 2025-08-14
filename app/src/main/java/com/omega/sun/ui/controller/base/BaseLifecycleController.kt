package com.omega.sun.ui.controller.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.os.MessageQueue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.archlifecycle.ControllerLifecycleOwner
import com.omega.resource.R
import com.omega.sun.ui.ContainerActivity
import com.omega.sun.ui.ext.checkControllerIsInTop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

// ------------------------------------------------------------------------
// 返回生命周期的Controller
// ------------------------------------------------------------------------
abstract class BaseLifecycleController(args: Bundle? = null) : BaseController(args), LifecycleOwner, CoroutineScope by MainScope(),ViewModelStoreOwner {

    private val lifecycleOwner = ControllerLifecycleOwner(this)
    private val controllerViewModel = ArrayList<ViewModel>(10)
    // ---- TAG ------
    val controllerTag = this::class.simpleName
    // ------------------------------------------------------------------------
    // 页面进场动画释放结束
    // ------------------------------------------------------------------------
    var isInitView = false
    // ---- controller 是否可见 ------
    var controllerViewIsInVisible = true
    private var isFromActivityPause = false
    // ---- 是否拦截触摸 ------
    open val isInterceptTouch = true
    // ---- 等待空闲时操�? ------
    private var idleHandlerTodoList = mutableListOf<MessageQueue.IdleHandler>()

    // ---- 是否强制停止改变可见�? ------
    var isForceStopChangeVisibility = false
    // ------------------------------------------------------------------------
    // 添加主线程非阻塞操作
    // ------------------------------------------------------------------------
    fun addIdleHandle(handle:()->Unit){
        val idleHandler = MessageQueue.IdleHandler{
            handle()
            false
        }
        Looper.getMainLooper().queue.addIdleHandler(idleHandler)
    }
    // ------------------------------------------------------------------------
    // 添加主线程非阻塞操作-超时执行
    // ------------------------------------------------------------------------
    fun addIdleHandle(timeOut:Long,handle:()->Unit){
        var isDoAction = false
        val idleHandler = MessageQueue.IdleHandler{
            isDoAction = true
            handle()
            false
        }
        view?.postDelayed(timeOut){
            if(!isDoAction){
                Looper.getMainLooper().queue.removeIdleHandler(idleHandler)
                handle()
            }
        }
        Looper.getMainLooper().queue.addIdleHandler(idleHandler)
    }

    init {
        watchForLeaks()
        addLifecycleListener(object : LifecycleListener() {
            override fun preCreateView(controller: Controller) {
                super.preCreateView(controller)
                onPreCreateView()
            }

            override fun postCreateView(controller: Controller, view: View) {
                super.postCreateView(controller, view)
                onViewCreated(view)
            }

            override fun onChangeEnd(controller: Controller, changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
                when(changeType){
                    ControllerChangeType.PUSH_ENTER -> {
                        if(!isInitView){
                            view?.let {
                                isInitView = true
                                onPageAnimEnd(it)
                            }
                        }
                    }

                    ControllerChangeType.PUSH_EXIT -> {
                        if(isInitView) somethingChangeVisible(false)
//                        if(BuildConfig.DEBUG) ToastUtils.showShort("${controller::class.simpleName} �?始进入不可见")
                    }
                    ControllerChangeType.POP_ENTER -> {
                        if(isInitView) somethingChangeVisible(true)
//                        if(BuildConfig.DEBUG) ToastUtils.showShort("${controller::class.simpleName} 重新恢复可见")
                    }
                    else -> Unit
                }
            }
        })
    }

    // ------------------------------------------------------------------------
    // 预加载数�?-在创建View之前
    // ------------------------------------------------------------------------
    open fun onPreCreateView() = Unit

    // ------------------------------------------------------------------------
    // 创建View
    // ------------------------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        val root = onCreateView(container.context)
        if (isInterceptTouch) {
            root.isClickable = true
        }
        root.setTag(R.id.view_finish_action, Runnable {
            releaseResource()
        })
        return root
    }

    abstract fun onCreateView(context: Context): View
    // ------------------------------------------------------------------------
    // View创建之后
    // ------------------------------------------------------------------------
    open fun onViewCreated(view: View){}
    // ------------------------------------------------------------------------
    // 进场动画执行完后
    // ------------------------------------------------------------------------
    open fun onPageAnimEnd(view: View) = Unit


    // ------------------------------------------------------------------------
    // 某些东西正在调整页面可见�?,�?般主要调这个
    // ------------------------------------------------------------------------
    fun somethingChangeVisible(visible: Boolean){
        if(visible && isForceStopChangeVisibility) return
        if(visible == controllerViewIsInVisible || !isInitView || isDestroyed || !isActive) return
        if(visible && (activity as? ContainerActivity)?.router?.checkControllerIsInTop(this) == true && (activity as AppCompatActivity).lifecycle.currentState != Lifecycle.State.RESUMED){
            isFromActivityPause = true
            return
        }
        controllerViewIsInVisible = visible
        isInvisibleChange(controllerViewIsInVisible)
    }
    // ------------------------------------------------------------------------
    // 某些东西正在调整页面可见性真正发生变�?,去重上面的调�?
    // ------------------------------------------------------------------------
    open fun isInvisibleChange(visible: Boolean){}

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        if(isFromActivityPause  && (activity as? ContainerActivity)?.router?.checkControllerIsInTop(this) == true ){
            somethingChangeVisible(true)
        }
        isFromActivityPause = false
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        if(controllerViewIsInVisible && (activity as? ContainerActivity)?.router?.checkControllerIsInTop(this) == true){
            isFromActivityPause = true
            somethingChangeVisible(false)
        }
    }

    override val lifecycle: Lifecycle
        get() = lifecycleOwner.lifecycle


    // ------------------------------------------------------------------------
    // Activity Scope
    // ------------------------------------------------------------------------
    private val activityViewModelProvider: ViewModelProvider by lazy(LazyThreadSafetyMode.NONE) {
        if(activity == null) ViewModelProvider(this)
        else ViewModelProvider(activity as AppCompatActivity)
    }
    fun <T : ViewModel> getActivityScopeViewModel(modelClass: Class<T>): T = activityViewModelProvider[modelClass]

    // ------------------------------------------------------------------------
    // Controller Scope
    // ------------------------------------------------------------------------
    private val controllerViewModelStore: ViewModelStore by lazy(LazyThreadSafetyMode.NONE) { ViewModelStore() }
    private val viewModelProvider: ViewModelProvider by lazy(LazyThreadSafetyMode.NONE) { ViewModelProvider(this) }
    fun <T : ViewModel> getControllerScopeViewMode(clazz: Class<T>): T = viewModelProvider[clazz].apply {
        controllerViewModel.add(this)
    }

    override val viewModelStore: ViewModelStore
        get() = controllerViewModelStore

    override fun onDestroyView(view: View) {
        releaseResource()
        super.onDestroyView(view)
    }

    private var hasReleaseResource = false
    private fun releaseResource(){
        if(!hasReleaseResource){
            hasReleaseResource = true
            // ---- 消�?�状�? ------
            isInitView = false
            // ---- 减少性能消�?�，取消执行消息队列 ------
            lifecycleScope.cancel()
            // ---- 移除监听Observers ------
            cancel()
            view?.setTag(R.id.view_finish_action,null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ----网络请求处理协程,放到�?后处�?, 减少性能消�?�，未发送配置变更的情况下，清除view model ------
        controllerViewModel.forEach {
            runCatching {
                it.viewModelScope.cancel()
            }
        }
    }
}
