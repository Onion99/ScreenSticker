package com.omega.sun.ui.controller.page

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.omega.sun.ui.controller.base.BaseLifecycleController

class HomeController : BaseLifecycleController()  {

    @Composable
    override fun ComposeUI() {
        TestText()
    }

    @Preview
    @Composable
    fun TestText(){
        Text("123123123")
    }
}