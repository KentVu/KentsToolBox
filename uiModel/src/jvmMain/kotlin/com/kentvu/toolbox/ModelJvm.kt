package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.JvmRoomRepository
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.MutableStateFlow

class ModelJvm(
  environment: Environment = Environment.Dev,
  private val repository: JvmRoomRepository = JvmRoomRepository(environment),
  /*private val coroutineScope: CoroutineScope,*/
) : Model {
  override val state = MutableStateFlow(State())

  override suspend fun get(path: String) {
    val items = with(repository) {
      Item.objects()
    }
    state.value = State("/", items)
  }

  override suspend fun post(
    path: String,
    item: Item
  ) {
    //coroutineScope.launch {}
    with(repository) {
      item.save()
    }
    // notifies downstream (UI)
    state.value = State("/", listOf(item))
  }
}