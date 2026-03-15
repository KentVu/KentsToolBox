package com.kentvu.toolbox.tests

import androidx.compose.runtime.mutableStateListOf
import com.kentvu.toolbox.Model
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.MutableStateFlow

class FakeModel(
    override val state: MutableStateFlow<State> = MutableStateFlow(State())
) : Model {
    var calledPost: Boolean = false
    var calledGetTimes: Int = 0
    val backstack = mutableStateListOf("/")

    override suspend fun post(path: String, item: Item) {
        calledPost = true
        state.value = State(path, listOf(item))
    }

    override suspend fun get(path: String) {
        calledGetTimes++
    }

}