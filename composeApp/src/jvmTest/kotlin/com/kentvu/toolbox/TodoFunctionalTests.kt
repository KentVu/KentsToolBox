package com.kentvu.toolbox

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import de.mannodermaus.junit5.compose.ComposeRuleExtension
import de.mannodermaus.junit5.compose.ComposeRuleRunner
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@DisplayName("Edith has heard about a cool new online to-do app.")
class TodoFunctionalTests {
    @JvmField
    @RegisterExtension
    val extension = ComposeRuleExtension()

    @Test
    fun `She goes to check out its homepage`(/*runner: ComposeRuleRunner*/) = extension.rule.run {
        setContent { App() }
    }

    @Test
    fun `She notices the page title and header mention to-do lists`(): Unit =
        extension.rule.run {
            onNodeWithTag("title").assert(hasText("To-Do"))
        }

}
