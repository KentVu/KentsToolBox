package com.kentvu.toolbox.tests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.kentvu.toolbox.DefaultHttpModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.View
import com.kentvu.toolbox.data.InMemDataSource
import com.kentvu.toolbox.ui.App
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * ```python
 * class ListViewTest(TestCase):
 *     def test_renders_input_form(self):
 *         response = self.client.get("/lists/the-only-list-in-the-world/")
 *         self.assertContains(response, '<form method="POST">')
 *         self.assertContains(response, '<input name="item_text"')
 *
 *     def test_displays_all_list_items(self):
 *         Item.objects.create(text="itemey 1")
 *         Item.objects.create(text="itemey 2")
 *
 *         response = self.client.get("/lists/the-only-list-in-the-world/")
 *
 *         self.assertContains(response, "itemey 1")
 *         self.assertContains(response, "itemey 2")
 * ```
 */
@OptIn(ExperimentalTestApi::class)
class ListViewTest : TodoUnitTest() {

  @Test
  fun test_uses_list_template() = runComposeUiTest {
    val model = SpyHttpModel(DefaultRepository(
      InMemDataSource(), InMemDataSource()
    ))
    val stateFlow = MutableStateFlow(State())
    val view = View.Default(model, stateFlow)
    setContent { App(view) }
    stateFlow.value = State("/lists/the-only-list-in-the-world/")
    assertTemplateUsed("list.html")
  }

  @Test
  fun test_renders_input_form() = runComposeUiTest {
    val model = SpyHttpModel(DefaultRepository(
      InMemDataSource(), InMemDataSource()
    ))
    val stateFlow = MutableStateFlow(State())
    val view = View.Default(model, stateFlow)
    setContent { App(view) }
    stateFlow.value = State("/lists/the-only-list-in-the-world/")
    onNodeWithTag("id_new_item").assertIsDisplayed()
    // Verify form's action URL
    view.handleNewItemSubmit("Itemey 1")
    assertEquals("/lists/new", model.calledPostPath)
  }

  @Test
  fun test_displays_all_list_items() = runComposeUiTest {
    val state = MutableStateFlow(State())
    val view = View.Default(DefaultHttpModel(DefaultRepository(
      InMemDataSource(), InMemDataSource()
    )), state)
    setContent { App(view) }
    state.value = State(
      path = "/lists/the-only-list-in-the-world/",
      data = listOf(
        Item("itemey 1"),
        Item("itemey 2"),
      )
    )
    onNodeWithText("itemey 1", true).assertIsDisplayed()
    onNodeWithText("itemey 2", true).assertIsDisplayed()
  }

}