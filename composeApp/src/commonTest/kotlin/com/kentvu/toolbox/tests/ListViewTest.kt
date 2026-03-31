package com.kentvu.toolbox.tests

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.kentvu.toolbox.ui.App
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test

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
class ListViewTest {

  @Test
  fun test_uses_list_template() = runComposeUiTest {
    val state = MutableStateFlow(State(path = "/lists/the-only-list-in-the-world/"))
    val model = FakeModel(state)
    setContent { App(model) }
    assertTemplateUsed("list.html")
  }

  @Test
  fun test_renders_input_form() = runComposeUiTest {
    val state = MutableStateFlow(State(path = "/lists/the-only-list-in-the-world/"))
    val model = FakeModel(MutableStateFlow(State(path = "/lists/the-only-list-in-the-world/")))
    setContent { App(model) }
    onNodeWithTag("id_new_item").assertIsDisplayed()
  }

  @Test
  fun test_displays_all_list_items() = runComposeUiTest {
    val state = MutableStateFlow(State(
      path = "/lists/the-only-list-in-the-world/",
      data = listOf(
        Item("itemey 1"),
        Item("itemey 2"),
      )
    ))
    val model = FakeModel(state)
    setContent { App(model) }
    onNodeWithText("itemey 1", true).assertIsDisplayed()
    onNodeWithText("itemey 2", true).assertIsDisplayed()
  }

  private fun ComposeUiTest.assertTemplateUsed(templateName: String) {
    onNodeWithTag(templateName).assertIsDisplayed()
  }

}