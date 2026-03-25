package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.application
import com.kentvu.toolbox.client.RemoteDataSource
import com.kentvu.toolbox.data.JvmRoomDatasource

fun main() = application {
    AppJvm(::exitApplication)
}

@Composable
fun AppJvm(onCloseRequest: () -> Unit = {}) {
    val environment: Environment = Environment.Production
    val model = DefaultModel(DefaultRepository(
        RemoteDataSource(),
        JvmRoomDatasource(environment),
    ))
    TodoWindow(
        onCloseRequest = onCloseRequest,
    ).content {
        App(model)
    }
}