package com.kentvu.toolbox.test

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import com.kentvu.toolbox.App
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.PendingException
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

@HiltAndroidTest
class TodoSteps(
    val composeRuleHolder: ComposeRuleHolder,
) : SemanticsNodeInteractionsProvider by composeRuleHolder.composeRule {

    //lateinit var compose: ComposeUiTest
//    val compose = DesktopComposeUiTest()
//    lateinit var quitSignal: CancellableContinuation<Unit>
//    val scope = CoroutineScope(Dispatchers.Default)

    init {
        /*When("She goes to check out its homepage") {
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
            //quitSignal.resume(Unit) { cause, _, _ -> error("Cancelled!") }
            //scope.launch {}
            //runTest { launch {} }
        }*/
        //suspendCancellableCoroutine { quitSignal = it }
        //delay(1000)
    }
    @When("She goes to check out its homepage")
    fun sheGoesToCheckOutItsHomepage() {
        //browser.get("http://localhost:8000")
        composeRuleHolder.composeRule.setContent {
            App()
        }
    }

    @Then("She notices the page title and header mention to-do lists")
    fun sheNoticesThePageTitleAndHeaderMentionToDoLists() {
        //assert "To-Do" in browser.title
        onNodeWithTag("title").assert(hasText("To-Do"))
    }

    @Then("She is invited to enter a to-do item straight away")
    fun sheIsInvitedToEnterAToDoItemStraightAway() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException("Finish the test!")
    }

    @Then("Satisfied, she goes back to sleep")
    fun satisfiedSheGoesBackToSleep() {
        //browser.quit()
        throw PendingException()
    }
}
