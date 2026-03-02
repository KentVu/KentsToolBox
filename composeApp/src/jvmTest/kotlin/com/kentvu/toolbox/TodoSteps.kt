package com.kentvu.toolbox

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.DesktopComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.kentvu.toolbox.App
import io.cucumber.java.Before
import io.cucumber.java.PendingException
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

@OptIn(ExperimentalTestApi::class)
class TodoSteps() {

    //lateinit var compose: ComposeUiTest
    val compose = DesktopComposeUiTest()
    /**
     * Because compose testing API won't allow keeping a [ComposeUiTest] instance outside of
     * [runComposeUiTest], this is the workaround to make Compose UI test work with Cucumber.
     */
    val steps = arrayOf<ComposeUiTest.() -> Unit>(
        {setContent {
            App()
        }},
        {onNodeWithTag("title").assert(hasText("To-Do"))}
    )
    val results = Array<Result<Unit>>(steps.size) { Result.success(Unit) }

    @Before
    fun runComposeTestAndCaptureExceptions() {
        runComposeUiTest {
            steps.forEachIndexed { index, step ->
                results[index] = runCatching {
                    step()
                }
            }
        }
    }

    @When("She goes to check out its homepage")
    fun sheGoesToCheckOutItsHomepage() {
        //browser.get("http://localhost:8000")
        results[0].getOrThrow()
    }

    @Then("She notices the page title and header mention to-do lists")
    fun sheNoticesThePageTitleAndHeaderMentionToDoLists() {
        //assert "To-Do" in browser.title
        results[1].getOrThrow()
    }

    @Then("She is invited to enter a to-do item straight away")
    fun sheIsInvitedToEnterAToDoItemStraightAway() {
        throw PendingException("Finish the test!")
    }

    @Then("Satisfied, she goes back to sleep")
    fun satisfiedSheGoesBackToSleep() {
        //browser.quit()
        throw PendingException()
    }
}
