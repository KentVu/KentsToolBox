package com.kentvu.toolbox

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KentsToolbox",
    ) {
        App()
    }
}