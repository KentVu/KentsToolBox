package com.kentvu.toolbox

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import de.mannodermaus.junit5.compose.ComposeRuleExtension
import de.mannodermaus.junit5.compose.ComposeRuleRunner
import org.junit.Rule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@DisplayName("Edith has heard about a cool new online to-do app.")
@ExtendWith(ComposeRuleExtension::class)
class TodoFunctionalTests {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `She goes to check out its homepage`(runner: ComposeRuleRunner) = runner.run {
        setContent { App() }
    }

    @Test
    fun `She notices the page title and header mention to-do lists`(runner: ComposeRuleRunner) =
        runner.run {
            onNodeWithTag("title").assert(hasText("To-Do"))
        }

}
