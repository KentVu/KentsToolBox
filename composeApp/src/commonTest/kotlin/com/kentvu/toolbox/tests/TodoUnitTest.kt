package com.kentvu.toolbox.tests

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag

abstract class TodoUnitTest {

  @OptIn(ExperimentalTestApi::class)
  protected fun ComposeUiTest.assertTemplateUsed(templateName: String) {
    onNodeWithTag(templateName).assertIsDisplayed()
  }
}