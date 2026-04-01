package com.kentvu.toolbox.tests

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.kentvu.toolbox.DefaultHttpModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.View
import com.kentvu.toolbox.data.InMemDataSource
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import com.kentvu.toolbox.ui.App
import kotlinx.coroutines.flow.toList
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class NewListTest {

  @Test
  fun test_can_save_a_POST_request() = runComposeUiTest {
    val fakeDataSource = InMemDataSource()
    val repo = DefaultRepository(fakeDataSource, InMemDataSource())
    val model = DefaultHttpModel(repo)
    val states = mutableListOf<State>()
    setContent {
      val view = View.Default(model)
      LaunchedEffect(1) {
        view.state.toList(states)
      }
      App(view)
    }
    // self.client.post("/lists/new", data={"item_text": "A new list item"})???

    onNodeWithTag("id_new_item").performTextInput("A new list item")
    onNodeWithTag("id_new_item").performImeAction()
    awaitIdle()
    with(repo) {
      assertEquals(1, fakeDataSource.itemsCount())
      val new_item = Item.objects().first()
      assertEquals("A new list item", new_item.text)
    }

    assertContains(states.last().data, Item("A new list item"))
    assertContains(states.last().path, "/lists/the-only-list-in-the-world/")
  }

  @Test
  fun test_redirects_after_POST() = runComposeUiTest {
    val fakeDataSource = InMemDataSource()
    val repo = DefaultRepository(fakeDataSource, InMemDataSource())
    val model = DefaultHttpModel(repo)
    val view = View.Default(model)
    setContent { App(view) }
    assertEquals("/", view.state.value.path)
    onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")
    onNodeWithTag("id_new_item").performImeAction()
    assertEquals("/lists/the-only-list-in-the-world/", view.state.value.path)
  }

}