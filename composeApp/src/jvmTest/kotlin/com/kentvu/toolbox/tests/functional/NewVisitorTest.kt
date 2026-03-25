package com.kentvu.toolbox.tests.functional

import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import com.kentvu.toolbox.AppJvm
import com.kentvu.toolbox.client.RemoteDataSource
import io.kotest.matchers.string.shouldMatch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class NewVisitorTest {

  @Test
  fun test_can_start_a_todo_list() = runComposeUiTest {
    //Given a blank to-do list
    RemoteDataSource().itemsClear()
    //@When("She goes to check out its homepage")
    setContent { AppJvm().content() }
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
    wait_for_row_in_list_table("1: Buy peacock feathers")
    //There is still a text box inviting her to add another item.
    //She enters "Use peacock feathers to make a fly"
    // (Edith is very methodical)
    inputBox = onNodeWithTag("id_new_item")
    inputBox.performTextInput("Use peacock feathers to make a fly")
    inputBox.performImeAction()
    // The page updates again, and now shows both items on her list
    wait_for_row_in_list_table("2: Use peacock feathers to make a fly")
    wait_for_row_in_list_table("1: Buy peacock feathers")
    //@Then("Satisfied, she goes back to sleep")
  }

  @Test
  fun test_start_a_new_todo_list_for_the_user() = runComposeUiTest {
    //Given a blank to-do list
    RemoteDataSource().itemsClear()
    val appJvm = AppJvm()
    setContent { appJvm.content() }
    // Edith starts a new to-do list
    val inputBox = onNodeWithTag("id_new_item")
    inputBox.performTextInput("Buy peacock feathers")
    inputBox.performImeAction()
    wait_for_row_in_list_table("1: Buy peacock feathers")

    // She notices that her list has a unique URL
    onNodeWithTag("id_list_id").assert(!hasText(""))
    appJvm.model.state.value.path shouldMatch Regex("/list/.+")
    val list_id = appJvm.model.state.value.path.removePrefix("/list/")
    onNodeWithTag("id_list_id").assertTextContains(list_id)
      .assertIsDisplayed()
  }

  private fun ComposeUiTest.check_for_row_in_list_table(row_text: String) {
    onNodeWithTag("id_list_table")
      .onChildren()
      .assertAny(hasText(row_text))
  }
  private fun ComposeUiTest.wait_for_row_in_list_table(row_text: String) {
    try {
      waitUntilAtLeastOneExists(hasText(row_text))
    } catch (e: ComposeTimeoutException) {
      onNodeWithTag("id_list_table").printToLog("DEBUG")
      throw e
    }
    check_for_row_in_list_table(row_text)
  }
}