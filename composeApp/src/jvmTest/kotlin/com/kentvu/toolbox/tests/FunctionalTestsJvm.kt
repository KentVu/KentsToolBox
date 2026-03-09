package com.kentvu.toolbox.tests

import androidx.compose.runtime.Composable
import com.kentvu.toolbox.TodoWindow
import kotlin.test.assertEquals

class FunctionalTestsJvm: FunctionalTestsCommon() {
    val todoWindow = TodoWindow({})

    @Composable
    override fun PlatformContentWrapper(block: @Composable () -> Unit) {
        super.PlatformContentWrapper(block)
        //todoWindow.content(block)
    }

    override fun platformSpecificAssertions() {
        assertEquals("To-Do", todoWindow.title)
    }

}