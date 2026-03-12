package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.JvmRoomRepository
import com.kentvu.toolbox.models.Model
import kotlinx.coroutines.flow.MutableStateFlow

class BackendJvm(
  private val repository: JvmRoomRepository = JvmRoomRepository(),
  /*private val coroutineScope: CoroutineScope,*/
) : Backend {
  override val model = MutableStateFlow(Model())

  override suspend fun post(
    path: String,
    item: Item
  ) {
    //coroutineScope.launch {}
    with(repository) {
      item.save()
    }
    // notifies downstream (UI)
    model.value = Model("/", listOf(item))
  }
}