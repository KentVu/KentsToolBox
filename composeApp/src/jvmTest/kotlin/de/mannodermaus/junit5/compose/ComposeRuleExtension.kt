package de.mannodermaus.junit5.compose

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.runner.Description

//https://github.com/mannodermaus/android-junit-framework/issues/234#issuecomment-950169571
class ComposeRuleExtension : ParameterResolver {

    val rule by lazy { createComposeRule() }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == ComposeRuleRunner::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return ComposeRuleRunner(
            rule,
            Description.createTestDescription(
                extensionContext.testClass.orElse(this::class.java),
                extensionContext.displayName
            )
        )
    }
}
