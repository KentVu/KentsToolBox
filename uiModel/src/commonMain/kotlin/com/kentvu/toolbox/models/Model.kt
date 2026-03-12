package com.kentvu.toolbox.models

data class Model(
    val path: String = "/",
    val data: List<Item> = emptyList(),
)