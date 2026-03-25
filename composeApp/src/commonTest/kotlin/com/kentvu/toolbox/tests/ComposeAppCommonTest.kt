package com.kentvu.toolbox.tests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyChild
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
import com.kentvu.toolbox.DefaultModel
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.DefaultAsserter.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class ComposeAppCommonTest {

    @Test
    fun ensureFrontendUsesBackend() = runComposeUiTest {
        val state = MutableStateFlow(State())
        val model = FakeModel(state)
        setContent { App(model) }
        onNodeWithText("Buy peacock feathers")
            .assertDoesNotExist()
        state.emit(State("/", listOf(Item("Buy peacock feathers"))))
        awaitIdle()
        onNodeWithTag("id_list_table")//.apply { printToLog("DEBUG") }
            .assert(hasAnyChild(hasText("Buy peacock feathers", true)))
    }

    @Test //the "contract" between the frontend and the backend.
    fun ensureBackendGetIsCalledOnInit() = runComposeUiTest {
        val state = MutableStateFlow(State())
        val model = FakeModel(state)
        setContent { App(model) }

        assertEquals(1, model.calledGetTimes)
    }

    @Test //the "contract" between the frontend and the backend.
    fun ensureBackendPostIsCalled() = runComposeUiTest {
        val state = MutableStateFlow(State())
        val model = FakeModel(state)
        setContent { App(model) }
        onNodeWithTag("id_list_table").onChildren().assertCountEquals(0)
        onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")

        //onNodeWithTag("id_new_item").performKeyInput { Key.Enter }
        onNodeWithTag("id_new_item").performImeAction()
        /*a*/waitForIdle()

        waitUntilNodeCount(hasAnyAncestor(hasTestTag("id_list_table")), 1)
        assertTrue(model.calledPost)
        onNodeWithTag("id_list_table").apply { printToLog("DEBUG") }
            .assert(hasAnyChild(hasText("Buy peacock feathers", true)))
    }

    @Test
    fun test_redirects_after_POST() = runComposeUiTest {
        val state = MutableStateFlow(State())
        val model = FakeModel()
        setContent { App(model) }
        assertFalse(model.calledPost, "backend.calledPost")
        assertEquals(1, model.calledGetTimes)

        onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")
        onNodeWithTag("id_new_item").performImeAction()

        assertTrue("backend.calledPost", model.calledPost)
        // "Redirect" means a reload after a POST.
        assertEquals(2, model.calledGetTimes, "A GET should be called after a POST")
        //assertEquals("/", backend.model.value.path)
    }

    @Test
    fun newItemTextFieldShouldClearAfterEnterPressed() = runComposeUiTest {
        val state = MutableStateFlow(State())
        val model = FakeModel(state)
        setContent { App(model) }

        onNodeWithTag("id_new_item").performTextInput("Buy peacock feathers")
        onNodeWithTag("id_new_item").performImeAction()

        onNodeWithTag("id_new_item").assert(hasText(""))
    }
}