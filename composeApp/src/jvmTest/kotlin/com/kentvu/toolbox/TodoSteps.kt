package com.kentvu.toolbox

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.DesktopComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.cucumber.java.PendingException
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.java8.En
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalTestApi::class)
class TodoSteps() : En {

    //lateinit var compose: ComposeUiTest
    val compose = DesktopComposeUiTest()
    lateinit var quitSignal: CancellableContinuation<Unit>
    val scope = CoroutineScope(Dispatchers.Default)

    init {
        When("She goes to check out its homepage") {
            //browser.get("http://localhost:8000")

            //RunCucumberTestJunit4.rule
            compose.runTest {
                setContent { App() }
            }
            // suspend until quitSignal
        }
        Then("She notices the page title and header mention to-do lists") {
            //assert "To-Do" in browser.title
            compose.onNodeWithTag("Title").assert(hasText("To-Do"))
            //scope.launch {}
            //runTest { launch {} }
        }
        Then("Satisfied, she goes back to sleep") {
            //browser.quit()
            //quitSignal.resume(Unit) { cause, _, _ -> error("Cancelled!") }
            //scope.launch {}
            //runTest { launch {} }
        }
        //suspendCancellableCoroutine { quitSignal = it }
        //delay(1000)
    }
    /*@When("She goes to check out its homepage")
    fun sheGoesToCheckOutItsHomepage() {
        //browser.get("http://localhost:8000")
        throw PendingException()
    }

    @Then("She notices the page title and header mention to-do lists")
    fun sheNoticesThePageTitleAndHeaderMentionToDoLists() {
        //assert "To-Do" in browser.title
        throw PendingException()
    }

    @Then("Satisfied, she goes back to sleep")
    fun satisfiedSheGoesBackToSleep() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }*/
}