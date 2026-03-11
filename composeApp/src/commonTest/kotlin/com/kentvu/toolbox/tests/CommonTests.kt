package com.kentvu.toolbox.tests

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.waitUntilNodeCount
import com.kentvu.toolbox.App
import com.kentvu.toolbox.Backend
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class CommonTests {

    class FakeBackend(override val model: MutableStateFlow<Model>) : Backend {
        var called: Boolean = false

        override suspend fun post(
            action: Backend.Action,
            item: Item
        ) {
            called = true
            model.value = Model(listOf(item))
        }

    }

    @Test
    fun ensureFrontendUsesBackend() = runComposeUiTest {
        val model = MutableStateFlow(Model())
        val backend = FakeBackend(model)
        setContent { App(backend) }
        onNodeWithText("Buy peacock feathers")
            .assertDoesNotExist()
        model.emit(Model(listOf(Item("Buy peacock feathers"))))
        awaitIdle()
        onNodeWithTag("id_list_table")//.apply { printToLog("DEBUG") }
            .assert(hasAnyChild(hasText("Buy peacock feathers", true)))
    }

    @Test //the "contract" between the frontend and the backend.
    fun ensureBackendIsCalled() = runComposeUiTest {
        val model = MutableStateFlow(Model())
        val backend = FakeBackend(model)
        setContent { App(backend) }
        onNodeWithTag("id_list_table").onChildren().assertCountEquals(0)
        onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")

        //onNodeWithTag("id_new_item").performKeyInput { Key.Enter }
        onNodeWithTag("id_new_item").performImeAction()
        /*a*/waitForIdle()

        waitUntilNodeCount(hasAnyAncestor(hasTestTag("id_list_table")), 1)
        assertTrue(backend.called)
        onNodeWithTag("id_list_table").apply { printToLog("DEBUG") }
            .assert(hasAnyChild(hasText("Buy peacock feathers", true)))
    }

}
