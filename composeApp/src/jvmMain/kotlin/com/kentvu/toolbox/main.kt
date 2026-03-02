package com.kentvu.toolbox

import androidx.compose.ui.window.application

fun main() = application {
    TodoWindow(
        onCloseRequest = ::exitApplication,
    ).content {
        App()
    }
}