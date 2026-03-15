package com.kentvu.toolbox.models

data class State(
    val path: String = "/",
    val data: List<Item> = emptyList(),
)