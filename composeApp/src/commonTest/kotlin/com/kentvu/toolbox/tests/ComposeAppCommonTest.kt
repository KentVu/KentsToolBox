package com.kentvu.toolbox.tests

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.waitUntilNodeCount
import com.kentvu.toolbox.DefaultModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.FakeRepo
import com.kentvu.toolbox.data.InMemDataSource
import com.kentvu.toolbox.ui.App
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import io.kotest.assertions.withClue
import io.kotest.matchers.string.shouldMatch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Drive UI changes. */
@OptIn(ExperimentalTestApi::class)
class ComposeAppCommonTest {

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
  fun test_can_save_a_POST_request() = runComposeUiTest {
    val fakeDataSource = InMemDataSource()
    val repo = DefaultRepository(fakeDataSource, InMemDataSource())
    val model = DefaultModel(repo)
    val states = mutableListOf<State>()
    setContent {
      LaunchedEffect(1) {
        model.state.toList(states)
      }
      App(model)
    }
    //coroutineScope { launch {} }
    //backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {}

    onNodeWithTag("id_new_item").performTextInput("A new list item")
    onNodeWithTag("id_new_item").performImeAction()
    with(repo) {
      assertEquals(1, fakeDataSource.itemsCount())
      val new_item = Item.objects().first()
      assertEquals("A new list item", new_item.text)
    }

    assertContains(states.last().data, Item("A new list item"))
    assertContains(states.last().path, "/lists/the-only-list-in-the-world/")
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
