package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.kentvu.toolbox.client.RemoteDataSource
import com.kentvu.toolbox.data.AndroidRoomDatasource

@Composable
fun AppAndroid() {
  val model = DefaultModel(DefaultRepository(
    AndroidRoomDatasource(LocalContext.current),
    RemoteDataSource(),
  ))
  App(model)
}

@Preview
@Composable
fun AppAndroidPreview() {
  App(Model.Preview())
}
