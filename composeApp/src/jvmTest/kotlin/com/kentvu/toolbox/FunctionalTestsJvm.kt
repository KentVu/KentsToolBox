package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import kotlin.test.assertEquals

class FunctionalTestsJvm: FunctionalTestsCommon() {
    val todoWindow = TodoWindow({})

    @Composable
    override fun PlatformContentWrapper(block: @Composable () -> Unit) {
        todoWindow.content(block)
    }

    override fun platformSpecificAssertions() {
        assertEquals("To-Do", todoWindow.title)
    }

}