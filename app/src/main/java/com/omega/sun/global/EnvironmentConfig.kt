package com.omega.sun.global

import android.Manifest
import java.io.File
import android.os.Build

object EnvironmentConfig {
    // ------------------------------------------------------------------------
    // 内部缓存路径
    // ------------------------------------------------------------------------
    var pathCacheIner = "/data/user/0/xxx/cache"
    // ------------------------------------------------------------------------
    // 外部缓存路径
     // ------------------------------------------------------------------------
    var pathCacheOuter = "/storage/emulated/0/Android/data/xxx/cache"

    // ------------------------------------------------------------------------
    // 外部数据存放路径
    // ------------------------------------------------------------------------
    var pathFileStorageOuter = "/storage/emulated/0/Android/data/xxx/file"

    // ------------------------------------------------------------------------
    // 相机权限
    // ------------------------------------------------------------------------
    var permissionCamera = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        arrayOf(Manifest.permission.CAMERA)
    else
        arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE)

    // ------------------------------------------------------------------------
    // 相册权限
    // ------------------------------------------------------------------------
    var permissionImage =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    else
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    // ------------------------------------------------------------------------
    // 视频权限
    // ------------------------------------------------------------------------
    var permissionVideoCall = arrayOf(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,)

    var uriFileType = "file://"


    const val OFFICIAL_URL =  "https://img.ahayuyin.com/MTY3Nzc1MTU5OTQ4MiMgOTIjanBn.jpg"
}
