package com.kentvu.toolbox.tests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.View
import com.kentvu.toolbox.data.InMemDataSource
import com.kentvu.toolbox.ui.App
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class NavigationTests {

  @Test
  fun testBackendNavigation() = runComposeUiTest {
    val view = View.Default(SpyHttpModel(DefaultRepository(
      InMemDataSource(), InMemDataSource()
    )))
    setContent { App(view) }
    onNodeWithContentDescription("Home screen").assertIsDisplayed()


    view.state.value = State(path = "/lists/the-only-list-in-the-world/", listOf(Item("A list item")))
    //backend.backstack.add("/second")
    onNodeWithText("A list item", true).assertIsDisplayed()
  }
}