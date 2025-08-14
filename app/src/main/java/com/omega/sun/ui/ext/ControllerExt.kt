package com.omega.sun.ui.ext

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bluelinelabs.conductor.Controller

/**
 * Created by lengyacheng on 2023/2/21
 * desc
 */
fun Controller.gotoCamera(uri: Uri?,requestCode:Int){
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    intent.putExtra("return-data", true)
    try {
        startActivityForResult(intent, requestCode)
    } catch (e: Exception) {
        Log.d("Controller", "gotoCamera: ${e.message}")
    }
}

fun Controller.getString(@StringRes resId: Int) = router.activity?.getString(resId) ?:""
fun Controller.getString(@StringRes resId: Int,vararg values: String) = router.activity?.getString(resId,*values) ?:""


@RequiresApi(Build.VERSION_CODES.N)
fun Uri.getImageDimensions(context: Context): Pair<Int, Int> {
    return kotlin.runCatching {
        val inputStream = context.contentResolver.openInputStream(this)!!
        val exif = ExifInterface(inputStream)
        val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 200)
        val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 200)
        Pair(width, height)
    }.getOrDefault(Pair(200, 200))
}

val STORAGE_REQUEST_CODE=10000
fun Controller.checkSelfPermission(permission: String): Boolean =  ContextCompat.checkSelfPermission(
    this.activity!!,
    permission
) == PackageManager.PERMISSION_GRANTED

fun Controller.isGrant(grantResults: IntArray): Boolean {
    if (grantResults.isEmpty()) return false
    return grantResults[0] == PackageManager.PERMISSION_GRANTED
}



