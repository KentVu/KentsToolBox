package com.kentvu.toolbox.tests

import androidx.compose.runtime.mutableStateListOf
import com.kentvu.toolbox.Backend
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Model
import kotlinx.coroutines.flow.MutableStateFlow

class FakeBackend(
    override val model: MutableStateFlow<Model> = MutableStateFlow(Model())
) : Backend {
    var calledPost: Boolean = false
    var calledGetTimes: Int = 0
    val backstack = mutableStateListOf("/")

    override suspend fun post(path: String, item: Item) {
        calledPost = true
        model.value = Model(path, listOf(item))
    }

    override suspend fun get(path: String) {
        calledGetTimes++
    }

}