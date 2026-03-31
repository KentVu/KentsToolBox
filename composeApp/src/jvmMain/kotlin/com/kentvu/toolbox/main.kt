package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.application
import com.kentvu.toolbox.client.RemoteDataSource
import com.kentvu.toolbox.data.JvmRoomDatasource
import com.kentvu.toolbox.ui.App

fun main() = application {
  AppJvm(::exitApplication).content()
}

class AppJvm(
  val onCloseRequest: () -> Unit = {},
  val model: DefaultModel = DefaultModel(
    DefaultRepository(
      RemoteDataSource(),
      JvmRoomDatasource(Environment.Production),
    )
  )
) {

  @Composable
  fun content() {
    TodoWindow(
      onCloseRequest = onCloseRequest,
    ).content {
      App(model)
    }
  }
}