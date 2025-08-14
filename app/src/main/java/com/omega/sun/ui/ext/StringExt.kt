package com.omega.sun.ui.ext

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.computeAge(): Int {
    return kotlin.runCatching {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = format.parse(this)
        return if (date != null) {
            val now = Date()
            now.year - date.year
        } else {
            0
        }
    }.getOrNull() ?: 0
}
