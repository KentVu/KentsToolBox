package com.kentvu.toolbox.tests

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import androidx.compose.ui.test.waitUntilNodeCount
import com.kentvu.toolbox.App
import com.kentvu.toolbox.Backend
import com.kentvu.toolbox.BackendJvm
import com.kentvu.toolbox.TodoWindow
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class FunctionalTestsJvm {

  @Test
  fun test_can_start_a_todo_list() = runComposeUiTest {
    val backend = BackendJvm(/*CoroutineScope(currentCoroutineContext())*/)
    //@When("She goes to check out its homepage")
    setContent { App(backend) }
    //@Then("She notices the page title and header mention to-do lists")
    onNodeWithTag("header").run {
      assert(hasText("To-Do"))
        .assertIsDisplayed()
      //onAncestors().onLast().printToLog("DEBUG")
    }
    //@Then("She is invited to enter a to-do item straight away")
    onNodeWithTag("id_new_item", true)
      .assert(hasAnyDescendant(hasTestTag("Placeholder") and hasText("Enter a to-do item")))
      .assertIsDisplayed()
    //@And("She types {string} into a text box")
    var inputBox = onNodeWithTag("id_new_item")
    inputBox.performTextInput("Buy peacock feathers")
    // When she hits enter, the page updates, and now the page lists
    //"1: Buy peacock feathers" as an item in a to-do list table
    inputBox.performImeAction()
    // or try this:
    //inputBox.performKeyInput { Key.Enter }
    waitUntilAtLeastOneExists(hasAnyAncestor(hasTestTag("id_list_table")))
    //or: waitUntil { onNodeWithTag("id_list_table").onChildren().fetchSemanticsNodes().isNotEmpty() }
    check_for_row_in_list_table("1: Buy peacock feathers")
    //There is still a text box inviting her to add another item.
    //She enters "Use peacock feathers to make a fly"
    // (Edith is very methodical)
    inputBox = onNodeWithTag("id_new_item")
    inputBox.performTextInput("Use peacock feathers to make a fly")
    inputBox.performImeAction()
    // The page updates again, and now shows both items on her list
    check_for_row_in_list_table("2: Use peacock feathers to make a fly")
    check_for_row_in_list_table("1: Buy peacock feathers")
    //@Then("Satisfied, she goes back to sleep")
  }

  private fun ComposeUiTest.check_for_row_in_list_table(row_text: String) {
    onNodeWithTag("id_list_table")
      .onChildren()
      .assertAny(hasText(row_text))
  }
}