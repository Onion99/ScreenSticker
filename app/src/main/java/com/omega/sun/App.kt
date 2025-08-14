package com.omega.sun

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// ---- 尽可能保�? Application 纯粹 ------
@HiltAndroidApp
class App : Application()
