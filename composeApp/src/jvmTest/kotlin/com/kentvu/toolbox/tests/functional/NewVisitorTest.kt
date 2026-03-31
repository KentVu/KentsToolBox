package com.kentvu.toolbox.tests.functional

import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import com.kentvu.toolbox.AppJvm
import com.kentvu.toolbox.DefaultModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.client.RemoteDataSource
import com.kentvu.toolbox.data.InMemDataSource
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import com.kentvu.toolbox.ui.App
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.string.shouldMatch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.test.Test
import kotlin.test.assertContains

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
    //She enters "Use peacock feathers to make a fly" (Edith is very methodical)
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
    onNodeWithTag("id_list_id").assert(!hasText("")) { "list id should not empty" }
    appJvm.model.state.value.path shouldMatch Regex("/lists/.+")
    val list_id = appJvm.model.state.value.path.removePrefix("/lists/").removeSuffix("/")
    onNodeWithTag("id_list_id").assertTextContains(list_id)
      .assertIsDisplayed()
  }

  @Test
  fun test_multiple_users_can_start_lists_at_different_urls() = runComposeUiTest {
    //Given a blank to-do list
    RemoteDataSource().itemsClear()
    val states = mutableListOf<State>()
    val appJvm = AppJvm()
    setContent {
      LaunchedEffect(1) {
        appJvm.model.state.toList(states)
      }
      appJvm.content()
    }
    // Edith starts a new to-do list
    onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")
    onNodeWithTag("id_new_item").performImeAction()
    wait_for_row_in_list_table("1: Buy peacock feathers")
    // She notices that her list has a unique URL
    withClue("Current page should has a unique URL") {
      states.last().path shouldMatch Regex("/lists/.+")
    }
    val list_id = Regex("/lists/(.+)/").find(states.last().path)?.groups[1]?.value!!
    onNodeWithTag("id_list_id").assertTextContains(list_id)
      .assertIsDisplayed()
    val edith_list_url = states.last().path
    // Now a new user, Francis, comes along to the site.
    //# We delete all the browser's cookies
    //# as a way of simulating a brand-new user session
    // Francis visits the home page.  There is no sign of Edith's list
    appJvm.model.get("/")
    states.last().data shouldNotContain Item("Buy peacock feathers")
    // Francis starts a new list by entering a new item. He
    // is less interesting than Edith...
    onNodeWithTag("id_new_item").performTextInput("Buy milk")
    onNodeWithTag("id_new_item").performImeAction()
    wait_for_row_in_list_table("1: Buy milk")
    withClue("Francis gets his own unique URL") {
      states.last().path shouldMatch Regex("/lists/.+")
    }
    val francis_list_url = states.last().path
    francis_list_url shouldNotBeEqual edith_list_url
    //Again, there is no trace of Edith's list
    onNodeWithText("Buy peacock feathers", true).assertDoesNotExist()
    onNodeWithText("Buy milk", true).assertExists()
    //Satisfied, they both go back to sleep
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
      // ignore error in logging code
      runCatching { onNodeWithTag("id_list_table").printToLog("DEBUG") }
      throw e
    }
    check_for_row_in_list_table(row_text)
  }
}