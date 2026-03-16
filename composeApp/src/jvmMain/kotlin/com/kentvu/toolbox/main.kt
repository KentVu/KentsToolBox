package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.application
import com.kentvu.toolbox.models.JvmRoomDatasource

fun main() = application {
    AppJvm(::exitApplication)
}

@Composable
fun AppJvm(onCloseRequest: () -> Unit = {}) {
    val backend = DefaultModel(DefaultRepository(
        JvmRoomDatasource(Environment.Production),
            RemoteDataSource(),
    ))
    TodoWindow(
        onCloseRequest = onCloseRequest,
    ).content {
        App(backend)
    }
}