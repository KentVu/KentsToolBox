package com.kentvu.toolbox.models

data class State(
    /** Simulate Uri's path. */
    val path: String = "/",
    val data: List<Item> = emptyList(),
)