package com.kentvu.toolbox.tests

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.waitUntilNodeCount
import com.kentvu.toolbox.DefaultHttpModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.HttpModel
import com.kentvu.toolbox.View
import com.kentvu.toolbox.data.InMemDataSource
import com.kentvu.toolbox.ui.App
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Drive UI changes. */
@OptIn(ExperimentalTestApi::class)
class HomePageTest : TodoUnitTest() {
  class SpyView(model: HttpModel, private val d: View = View.Default(model)) : View by d {
    internal var calledOnInit = false
    internal var calledHandleNewItemSubmit: Boolean = false

    override suspend fun onInit() {
      calledOnInit = true
      d.onInit()
    }
    override suspend fun handleNewItemSubmit(text: String) {
      calledHandleNewItemSubmit = true
      d.handleNewItemSubmit(text)
    }
  }

  @Test //the "contract" between the frontend and the backend.
  fun ensureBackendGetIsCalledOnInit() = runComposeUiTest {
    val model = SpyHttpModel(DefaultHttpModel( DefaultRepository(
      InMemDataSource(), InMemDataSource()
    ) ))
    val view = SpyView(model)
    setContent { App(view) }

    assertTrue(view.calledOnInit, "view.calledOnInit")
    assertEquals(1, model.calledGetTimes)
  }

  @Test //the "contract" between the frontend and the backend.
  fun ensureBackendPostIsCalled() = runComposeUiTest {
    val model = SpyHttpModel(DefaultHttpModel( DefaultRepository(
      InMemDataSource(), InMemDataSource()
    ) ))
    val view = SpyView(model)
    setContent { App(view) }

    onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")
    //onNodeWithTag("id_new_item").performKeyInput { Key.Enter }
    onNodeWithTag("id_new_item").performImeAction()
    /*a*/waitForIdle()

    assertTrue(view.calledHandleNewItemSubmit, "view.calledHandleNewItemSubmit is FALSE")
    assertTrue(model.calledPost, "model.calledPost is FALSE")
  }

  @Test
  fun test_uses_home_template() = runComposeUiTest {
    val fakeDataSource = InMemDataSource()
    val repo = DefaultRepository(fakeDataSource, InMemDataSource())
    val model = DefaultHttpModel(repo)
    val view = View.Default(model)
    setContent { App(view) }
    assertEquals("/", view.state.value.path)
    assertTemplateUsed("home.html")
  }

  @Test
  fun test_renders_input_form() = runComposeUiTest {
    val model = SpyHttpModel(DefaultHttpModel(DefaultRepository(
      InMemDataSource(), InMemDataSource()
    )))
    val view = View.Default(model)
    setContent { App(view) }
    onNodeWithTag("id_new_item").assertIsDisplayed()
    // Verify form's action URL
    view.handleNewItemSubmit("Itemey 1")
    assertEquals("/lists/new", model.calledPostPath)
  }

  @Test
  fun newItemTextFieldShouldClearAfterEnterPressed() = runComposeUiTest {
    val view = View.Default(DefaultHttpModel(DefaultRepository(
      InMemDataSource(), InMemDataSource()
    )))
    setContent { App(view) }

    onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")
    onNodeWithTag("id_new_item").performImeAction()

    onNodeWithTag("id_new_item").assert(hasText(""))
  }

  private fun ComposeUiTest.waitForItemAppeared() {
    waitUntilNodeCount(hasAnyAncestor(hasTestTag("id_list_table")), 1)
  }
}
