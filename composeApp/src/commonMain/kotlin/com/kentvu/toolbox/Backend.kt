package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Simulates a web Backend, but this only cares about the UI model, not the UI itself.
 */
interface Backend {
    enum class Action {
        Add
    }
    val model: StateFlow<Model>

    fun post(action: Action, item: Item)

    class Default : Backend {
        // wait for explicit-backing-property to be stable to simplify this.
        private val _model = MutableStateFlow(Model())
        override val model: StateFlow<Model>
            get() = _model

        override fun post(action: Action, item: Item) {
            _model.value = Model(listOf(item))
        }
    }
}
