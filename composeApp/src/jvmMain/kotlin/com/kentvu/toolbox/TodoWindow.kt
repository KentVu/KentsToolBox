package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window

class TodoWindow(val onCloseRequest: () -> Unit) {

    val title = "To-Do"

    @Composable
    fun content(
        content: @Composable FrameWindowScope.() -> Unit
    ) {
        Window(
            onCloseRequest = onCloseRequest,
            title = title,
        ) {
            content()
        }
    }
}