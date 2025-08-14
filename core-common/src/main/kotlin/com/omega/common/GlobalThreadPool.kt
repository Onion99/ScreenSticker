package com.omega.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

// ------------------------------------------------------------------------
// corePoolSize �? 保留在池中的线程数，即使它们处于空闲状�?�，除非设置了allowCoreThreadTimeOut
// maximumPoolSize �? 池中允许的最大线程数 keepAliveTime当线程数大于核心数时，这是多余的空闲线程在终止前等待新任务的�?长时间�??
// unit �? keepAliveTime参数的时间单�?
// workQueue在执行任务之前用于保存任务的队列。该队列将仅保存由execute方法提交的Runnable任务�?
// threadFactory执行者创建新线程时使用的工厂
// ------------------------------------------------------------------------
val CPU_COUNT = Runtime.getRuntime().availableProcessors()
private val CORE_POOL_SIZE = (CPU_COUNT + 1).coerceAtLeast(4)
private val MAXIMUM_POOL_SIZE = (CPU_COUNT * 2 + 1).coerceAtLeast(4)
private const val KEEP_ALIVE = 10L
private val threadFactory: ThreadFactory = object : ThreadFactory {
    private val mCount: AtomicInteger = AtomicInteger(1)
    override fun newThread(r: Runnable): Thread{
        return Thread(r, "Nova Async Task #" + mCount.getAndIncrement())
    }
}


val GLOBAL_THREAD_POOL_EXECUTOR = ThreadPoolExecutor(
    MAXIMUM_POOL_SIZE, MAXIMUM_POOL_SIZE * 2, KEEP_ALIVE,
    TimeUnit.SECONDS, LinkedBlockingQueue(), threadFactory
)
