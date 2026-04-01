package com.kentvu.toolbox.tests

import androidx.compose.runtime.LaunchedEffect
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
import com.kentvu.toolbox.DefaultModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.data.InMemDataSource
import com.kentvu.toolbox.ui.App
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Drive UI changes. */
@OptIn(ExperimentalTestApi::class)
class HomePageTest : TodoUnitTest() {

  @Test //the "contract" between the frontend and the backend.
  fun ensureBackendGetIsCalledOnInit() = runComposeUiTest {
    val state = MutableStateFlow(State())
    val model = FakeModel(state)
    setContent { App(model) }

    assertEquals(1, model.calledGetTimes)
  }

  @Test //the "contract" between the frontend and the backend.
  fun ensureBackendPostIsCalled() = runComposeUiTest {
    val state = MutableStateFlow(State())
    val model = FakeModel(state)
    setContent { App(model) }

    onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")
    //onNodeWithTag("id_new_item").performKeyInput { Key.Enter }
    onNodeWithTag("id_new_item").performImeAction()
    /*a*/waitForIdle()

    //waitForItemAppeared()
    assertTrue(model.calledPost, "model.calledPost is FALSE")
  }

  @Test
  fun test_uses_home_template() = runComposeUiTest {
    val fakeDataSource = InMemDataSource()
    val repo = DefaultRepository(fakeDataSource, InMemDataSource())
    val model = DefaultModel(repo)
    setContent { App(model) }
    assertEquals("/", model.state.value.path)
    assertTemplateUsed("home.html")
  }

  @Test
  fun test_renders_input_form() = runComposeUiTest {
    val model = FakeModel(MutableStateFlow(State(path = "/")))
    setContent { App(model) }
    onNodeWithTag("id_new_item").assertIsDisplayed()
  }

  @Test
  fun test_only_saves_items_when_necessary() = runComposeUiTest {
    val fakeDataSource = InMemDataSource()
    val repo = DefaultRepository(fakeDataSource, InMemDataSource())
    val model = DefaultModel(repo)
    setContent { App(model) }
    with(repo) {
      assertEquals(0, Item.objects().count())
    }
  }

  @Test
  fun newItemTextFieldShouldClearAfterEnterPressed() = runComposeUiTest {
    val state = MutableStateFlow(State())
    val model = FakeModel(state)
    setContent { App(model) }

    onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")
    onNodeWithTag("id_new_item").performImeAction()

    onNodeWithTag("id_new_item").assert(hasText(""))
  }

  private fun ComposeUiTest.waitForItemAppeared() {
    waitUntilNodeCount(hasAnyAncestor(hasTestTag("id_list_table")), 1)
  }
}
