package com.kentvu.toolbox

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.DesktopComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import io.cucumber.java.Before
import io.cucumber.java.PendingException
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class TodoSteps() {

    enum class StepOrder {
        One, Two, Three, Four, Five, Six, Seven
    }
    val compose = DesktopComposeUiTest()
    val todoWindow = TodoWindow({})
    lateinit var inputBox: SemanticsNodeInteraction

    /**
     * Because compose testing API won't allow keeping a [ComposeUiTest] instance outside of
     * [runComposeUiTest], this is the workaround to make Compose UI test work with Cucumber.
     * **The workaround disables ability to pass parameter to step function :(**
     */
    val steps = mapOf<StepOrder, ComposeUiTest.(/*Any*/) -> Unit>(
        StepOrder.One to {
            // While we're in JVM Let's spawn a window
            setContent { todoWindow.content { App() } }
            //setContent { App() }
            //onRoot().printToLog("DEBUG")
        },
        StepOrder.Two to {
            onNodeWithTag("header").run {
                assert(hasText("To-Do"))
                    .assertIsDisplayed()
                //onAncestors().onLast().printToLog("DEBUG")
            }
            assertEquals("To-Do", todoWindow.title)
        },
        StepOrder.Three to {
            onNodeWithTag("id_new_item")
                .assert(hasContentDescription("Placeholder") and hasAnyDescendant(hasText("Enter a to-do item")))
                .assertIsDisplayed()
        },
        StepOrder.Four to {
            inputBox = onNodeWithTag("id_new_item")
            //inputBox.performTextInput("$it")
            inputBox.performTextInput("Buy peacock feathers")
        },
        StepOrder.Five to {
            inputBox.performKeyPress(KeyEvent(Key.Enter))
        },
        StepOrder.Six to {
            onNodeWithTag("id_list_table")
                .assert(hasAnyChild(hasText("1: Buy peacock feathers")))
        }
    )
    //val results = steps.keys.associateWith { Result.success(Unit) }
    val results = mutableMapOf<StepOrder, Result<Unit>>()

    @Before
    fun runComposeTestAndCaptureExceptions() {
        runComposeUiTest {
            steps.forEach { (order, step) ->
                results[order] = runCatching {
                    step()
                }
            }
        }
    }

    @When("She goes to check out its homepage")
    fun sheGoesToCheckOutItsHomepage() {
        //browser.get("http://localhost:8000")
        results.getValue(StepOrder.One).getOrThrow()
    }

    @Then("She notices the page title and header mention to-do lists")
    fun sheNoticesThePageTitleAndHeaderMentionToDoLists() {
        //assert "To-Do" in browser.title
        results.getValue(StepOrder.Two).getOrThrow()
    }

    @Then("She is invited to enter a to-do item straight away")
    fun sheIsInvitedToEnterAToDoItemStraightAway() {
        results.getValue(StepOrder.Three).getOrThrow()
    }

    @And("She types {string} into a text box")
    fun sheTypesIntoATextBox(arg0: String?) {
        println("arg0: $arg0")
        results.getValue(StepOrder.Four).getOrThrow()
    }

    @When("she hits enter, the page updates")
    fun sheHitsEnterThePageUpdates() {
        results.getValue(StepOrder.Five).getOrThrow()
    }

    @And("now the page lists {string} as an item in a to-do list")
    fun nowThePageListsAsAnItemInAToDoList(arg0: String?) {
        println("arg0: $arg0")
        results.getValue(StepOrder.Six).getOrThrow()
    }

    @And("There is still a text box inviting her to add another item.")
    fun thereIsStillATextBoxInvitingHerToAddAnotherItem() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException("Finish the test!")
    }

    @Then("Satisfied, she goes back to sleep")
    fun satisfiedSheGoesBackToSleep() {
        //browser.quit()
        throw PendingException()
    }
}
