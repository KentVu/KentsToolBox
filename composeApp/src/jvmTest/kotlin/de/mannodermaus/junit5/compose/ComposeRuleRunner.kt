package de.mannodermaus.junit5.compose

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ComposeRuleRunner(private val rule: ComposeContentTestRule, private val description: Description) {
    fun run(action: ComposeContentTestRule.() -> Unit) {
        rule
            .apply(
                object : Statement() {
                    override fun evaluate() {
                        action.invoke(rule)
                    }
                },
                description
            )
            .evaluate()
    }
}