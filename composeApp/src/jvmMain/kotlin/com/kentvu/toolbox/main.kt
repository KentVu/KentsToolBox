package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.application

fun main() = application {
    AppJvm(::exitApplication)
}

@Composable
fun AppJvm(onCloseRequest: () -> Unit = {}) {
    val backend = ModelJvm(Environment.Production)
    TodoWindow(
        onCloseRequest = onCloseRequest,
    ).content {
        App(backend)
    }
}