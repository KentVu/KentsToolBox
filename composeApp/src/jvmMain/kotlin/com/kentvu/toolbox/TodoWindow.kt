package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window

class TodoWindow(val onCloseRequest: () -> Unit) {

    var title by mutableStateOf("To-Do")

    @Composable
    fun content(
        content: @Composable () -> Unit
    ) {
        Window(
            onCloseRequest = onCloseRequest,
            title = title,
        ) {
            content()
        }
    }
}