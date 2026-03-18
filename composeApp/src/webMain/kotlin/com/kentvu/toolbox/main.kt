package com.kentvu.toolbox

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val localDataSource = JsDatasource()
    ComposeViewport {
        App(DefaultModel(
            DefaultRepository(localDataSource, localDataSource)
        ))
    }
}