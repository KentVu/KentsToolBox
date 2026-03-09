package com.kentvu.toolbox.tests

import androidx.compose.runtime.Composable
import com.kentvu.toolbox.TodoWindow
import kotlin.test.assertEquals

val todoWindow = TodoWindow({})

@Composable
fun PlatformContentWrapper(block: @Composable () -> Unit) {
    block()
    todoWindow.content(block)
}

fun platformSpecificAssertions() {
    assertEquals("To-Do", todoWindow.title)
}
