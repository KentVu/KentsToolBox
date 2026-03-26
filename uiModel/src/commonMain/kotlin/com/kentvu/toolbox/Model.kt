package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Simulates a web Backend serving a REST-like API. This only cares about the UI model, not the
 * UI itself.
 */
interface Model {
    val state: StateFlow<State>

    /** [item] should be some sort of "Request" type, but I'm trying to keep things simple. */
    suspend fun post(path: String, item: Item)
    suspend fun get(path: String)

    class Preview : Model {
        // wait for explicit-backing-property to be stable to simplify this.
        private val _state = MutableStateFlow(State())
        override val state: StateFlow<State>
            get() = _state

        override suspend fun post(path: String, item: Item) {
            _state.value = State("/", listOf(item))
        }

        override suspend fun get(path: String) {
            _state.value = State("/lists/a-list/", listOf(Item("Buy peacock feathers")))
        }
    }
}