package com.kentvu.toolbox

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(plugin = ["pretty", "message:target/cucumber-report.ndjson"])
class RunCucumberTestJunit4 {
    companion object {
        val rule: ComposeContentTestRule = createComposeRule()
    }
    @get:Rule
    val rule = Companion.rule

    init {
        //Companion.rule = rule
    }
}