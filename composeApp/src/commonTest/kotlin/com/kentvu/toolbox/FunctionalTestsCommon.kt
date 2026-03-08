package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

abstract class FunctionalTestsCommon {

    class FakeBackend(override val model: StateFlow<Model>) : Backend {
        var called: Boolean = false

        override fun post(
            action: Backend.Action,
            item: Item
        ): Response {
            called = true
            return Response(emptyList())
        }

    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun ensureFrontendUsesBackend() = runComposeUiTest {
        val model = MutableStateFlow(Model())
        val backend = FakeBackend(model)
        setContent { PlatformContentWrapper { App(backend) } }
        onNodeWithText("1: Buy peacock feathers")
            .assertDoesNotExist()
        model.emit(Model(listOf(Item("Buy peacock feathers"))))
        awaitIdle()
        onNodeWithTag("id_list_table")//.apply { printToLog("DEBUG") }
            .assert(hasAnyChild(hasText("1: Buy peacock feathers")))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun test_can_start_a_todo_list() = runComposeUiTest{
        //@When("She goes to check out its homepage")
        val backend = Backend.Default()
        setContent { PlatformContentWrapper { App(backend) } }
        //@Then("She notices the page title and header mention to-do lists")
        onNodeWithTag("header").run {
            assert(hasText("To-Do"))
                .assertIsDisplayed()
            //onAncestors().onLast().printToLog("DEBUG")
        }
        platformSpecificAssertions()
        //@Then("She is invited to enter a to-do item straight away")
        onNodeWithTag("id_new_item", true)
            .assert(hasAnyDescendant(hasTestTag("Placeholder") and hasText("Enter a to-do item")))
            .assertIsDisplayed()
        //@And("She types {string} into a text box")
        val inputBox = onNodeWithTag("id_new_item")
        //inputBox.performTextInput("$it")
        inputBox.performTextInput("Buy peacock feathers")
        //@When("she hits enter, the page updates")
        //inputBox.performKeyPress(KeyEvent(Key.Enter))
        inputBox.performKeyInput { Key.Enter }
        // or try this:
        //inputBox.performImeAction()
        //@And("now the page lists {string} as an item in a to-do list")
        onNodeWithTag("id_list_table")
            .assert(hasAnyChild(hasText("1: Buy peacock feathers"))) {
                "New to-do item did not appear in table"}
        //@And("There is still a text box inviting her to add another item.")
        fail("Finish the test!")
        //@Then("Satisfied, she goes back to sleep")
    }

    protected open fun platformSpecificAssertions() {}

    @Composable
    protected open fun PlatformContentWrapper(block: @Composable () -> Unit) {
        block()
    }

}
