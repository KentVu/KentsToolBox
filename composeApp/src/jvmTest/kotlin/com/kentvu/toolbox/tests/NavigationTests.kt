package com.kentvu.toolbox.tests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.runComposeUiTest
import com.kentvu.toolbox.App
import com.kentvu.toolbox.BackendJvm
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class NavigationTests {

  // TODO replace with actual second screen when we have it.
  @Test
  fun testBackendNavigation() = runComposeUiTest {
    val backend = FakeBackend(
      MutableStateFlow(Model(path = "/", emptyList()))
    )
    setContent { App(backend) }
    onNodeWithContentDescription("Home screen").assertIsDisplayed()


    backend.model.value = Model(path = "/second", emptyList())
    //backend.backstack.add("/second")
    onNodeWithContentDescription("Second screen").assertIsDisplayed()
  }
}