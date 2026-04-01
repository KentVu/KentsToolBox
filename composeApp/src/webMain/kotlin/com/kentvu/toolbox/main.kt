package com.kentvu.toolbox

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kentvu.toolbox.ui.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val localDataSource = JsDatasource()
    ComposeViewport {
      App(
        DefaultHttpModel(
          DefaultRepository(localDataSource, localDataSource)
        )
      )
    }
}