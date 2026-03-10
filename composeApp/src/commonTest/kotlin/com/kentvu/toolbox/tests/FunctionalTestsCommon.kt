package com.kentvu.toolbox.tests

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.printToString
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.waitUntilNodeCount
import com.kentvu.toolbox.App
import com.kentvu.toolbox.Backend
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalTestApi::class)
class FunctionalTestsCommon {

    class FakeBackend(override val model: MutableStateFlow<Model>) : Backend {
        var called: Boolean = false

        override fun post(
            action: Backend.Action,
            item: Item
        ) {
            called = true
            model.value = Model(listOf(item))
        }

    }

    @Test
    fun ensureFrontendUsesBackend() = runComposeUiTest {
        val model = MutableStateFlow(Model())
        val backend = FakeBackend(model)
        setContent { App(backend) }
        onNodeWithText("Buy peacock feathers")
            .assertDoesNotExist()
        model.emit(Model(listOf(Item("Buy peacock feathers"))))
        awaitIdle()
        onNodeWithTag("id_list_table")//.apply { printToLog("DEBUG") }
            .assert(hasAnyChild(hasText("Buy peacock feathers", true)))
    }

    @Test //the "contract" between the frontend and the backend.
    fun ensureBackendIsCalled() = runComposeUiTest {
        val model = MutableStateFlow(Model())
        val backend = FakeBackend(model)
        setContent { App(backend) }
        onNodeWithTag("id_list_table").onChildren().assertCountEquals(0)
        onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")

        //onNodeWithTag("id_new_item").performKeyInput { Key.Enter }
        onNodeWithTag("id_new_item").performImeAction()
        /*a*/waitForIdle()

        waitUntilNodeCount(hasAnyAncestor(hasTestTag("id_list_table")), 1)
        assertTrue(backend.called)
        onNodeWithTag("id_list_table").apply { printToLog("DEBUG") }
            .assert(hasAnyChild(hasText("Buy peacock feathers", true)))
    }

    @Test
    fun test_can_start_a_todo_list() = runComposeUiTest{
        //@When("She goes to check out its homepage")
        val backend = Backend.Default()
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
        //inputBox.performTextInput("$it")
        inputBox.performTextInput("Buy peacock feathers")
        //@When("she hits enter, the page updates")
        //inputBox.performKeyPress(KeyEvent(Key.Enter))
        inputBox.performImeAction()
        // or try this:
        //inputBox.performKeyInput { Key.Enter }
        //@And("now the page lists "1: Buy peacock feathers" as an item in a to-do list")
        //waitForIdle()
        //waitUntilNodeCount(hasAnyAncestor(hasTestTag("id_list_table")), 1)
        //delay(1000)
        var log: String
        onNodeWithTag("id_list_table").apply { log = printToString() }
            .assert(hasAnyChild(hasText("1: Buy peacock feathers"))) {
                "New to-do item did not appear in table. Content were:\n$log"}
        //There is still a text box inviting her to add another item.
        //She enters "Use peacock feathers to make a fly"
        // (Edith is very methodical)
        inputBox = onNodeWithTag("id_new_item")
        inputBox.performTextInput("Use peacock feathers to make a fly")
        inputBox.performImeAction()
        // The page updates again, and now shows both items on her list
        onNodeWithTag("id_list_table")
            .onChildren()
            .assertAny(hasText("2: Use peacock feathers to make a fly"))
            .assertAny(hasText("1: Buy peacock feathers"))
        //@Then("Satisfied, she goes back to sleep")
    }

}
