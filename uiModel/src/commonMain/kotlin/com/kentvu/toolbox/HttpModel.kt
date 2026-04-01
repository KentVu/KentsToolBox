package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State

/**
 * Simulates a web Backend serving a REST-like API. This only cares about the UI model, not the
 * UI itself.
 * A new state should be updated after each [get]/[post] call to simulate a new URL.
 */
interface HttpModel {

    /**
     * [item] should be some sort of "Request" type, but I'm trying to keep things simple.
     * Return should be some sort of "Response" type, but I'm trying to keep things simple.
     */
    suspend fun post(path: String, item: Item): State
    suspend fun get(path: String): State

    class Preview : HttpModel {

        override suspend fun post(path: String, item: Item): State {
            return State("/", listOf(item))
        }

        override suspend fun get(path: String): State {
            return State("/lists/the-only-list-in-the-world/", listOf(Item("Buy peacock feathers")))
        }
    }
}