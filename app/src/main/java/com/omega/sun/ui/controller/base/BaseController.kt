package com.omega.sun.ui.controller.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.bluelinelabs.conductor.Controller

abstract class BaseController(args: Bundle? = null) : Controller(args) {


    val fragmentActivity: FragmentActivity?
        get() {
            return if (activity is FragmentActivity) activity as FragmentActivity else null
        }

    fun requireActivity(): FragmentActivity = activity as? FragmentActivity ?: throw IllegalStateException("$this not attached to an activity.")

    fun context(): Context? = view?.context

    fun requireContext(): Context = view?.context ?: activity ?: throw IllegalStateException("$this not attached to a context.")
}
