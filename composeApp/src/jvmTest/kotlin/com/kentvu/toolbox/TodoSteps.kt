package com.kentvu.toolbox

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.cucumber.java8.En
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalTestApi::class)
class TodoSteps : En {

    //@get:Rule val rule = createComposeRule()
    lateinit var compose: ComposeUiTest
    lateinit var quitSignal: CancellableContinuation<Unit>
    val scope = CoroutineScope(Dispatchers.Default)

    init {
        runComposeUiTest {
            When("She goes to check out its homepage") {
                //browser.get("http://localhost:8000")
                compose = this
                setContent { App() }
                // suspend until quitSignal
                //suspendCancellableCoroutine { quitSignal = it }
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
                scope.launch {}
                //runTest { launch {} }
            }
        }
    }
}