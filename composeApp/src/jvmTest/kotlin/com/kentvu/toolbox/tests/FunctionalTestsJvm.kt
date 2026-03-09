package com.kentvu.toolbox.tests

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.kentvu.toolbox.App
import com.kentvu.toolbox.Backend
import com.kentvu.toolbox.TodoWindow
import kotlin.test.Test
import kotlin.test.assertEquals

class FunctionalTestsJvm {
    val todoWindow = TodoWindow({})

    @Composable
    fun PlatformContentWrapper(block: @Composable () -> Unit) {
        todoWindow.content(block)
    }

    fun platformSpecificAssertions() {
        assertEquals("To-Do", todoWindow.title)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun windowsShouldHaveRightTitle() = runComposeUiTest {
        setContent { PlatformContentWrapper { App(Backend.Default()) } }
        platformSpecificAssertions()
    }
}