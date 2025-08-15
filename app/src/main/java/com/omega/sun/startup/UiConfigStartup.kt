package com.omega.sun.startup

import android.content.Context
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.omega.sun.global.EnvironmentConfig
import com.rousetime.android_startup.AndroidStartup
import com.tencent.mmkv.MMKV


class UiConfigStartup : AndroidStartup<String>() {

    override fun callCreateOnMainThread(): Boolean = true

    override fun create(context: Context): String? {
        MMKV.initialize(context)
        // Path初始�?
        EnvironmentConfig.pathCacheIner = context.cacheDir.absolutePath
        EnvironmentConfig.pathCacheOuter = context.externalCacheDir?.absolutePath ?: ""
        EnvironmentConfig.pathFileStorageOuter = context.getExternalFilesDir("media")?.absolutePath ?: ""
        // fresco初始�?
        Fresco.initialize(context)
        return this.javaClass.simpleName
    }

    override fun waitOnMainThread(): Boolean = false


    private fun getImageDiskCacheConfig(context: Context): DiskCacheConfig? {
        return DiskCacheConfig.newBuilder(context)
            .setMaxCacheSize((1000 * ByteConstants.MB).toLong())
            .setMaxCacheSizeOnLowDiskSpace((50 * ByteConstants.MB).toLong())
            .setMaxCacheSizeOnVeryLowDiskSpace((25 * ByteConstants.MB).toLong())
            .setVersion(0)
            .build()
    }
}

