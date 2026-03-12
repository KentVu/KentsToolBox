package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class JvmAdditionalTests {

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
    setContent { PlatformContentWrapper { App(Backend.Preview()) } }
    platformSpecificAssertions()
  }
}