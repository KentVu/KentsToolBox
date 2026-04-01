package com.kentvu.toolbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.kentvu.toolbox.client.RemoteDataSource
import com.kentvu.toolbox.data.AndroidRoomDatasource
import com.kentvu.toolbox.ui.App

@Composable
fun AppAndroid() {
  val model = DefaultHttpModel(DefaultRepository(
    RemoteDataSource(),
    AndroidRoomDatasource(LocalContext.current),
  ))
  App(model)
}

@Preview
@Composable
fun AppAndroidPreview() {
  App(HttpModel.Preview())
}
