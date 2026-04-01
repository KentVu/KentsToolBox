package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Repository
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultModel(
  private val repository: Repository,
) : Model {
  override val state = MutableStateFlow(State())

  override suspend fun get(path: String) {
    val items = with(repository) {
      Item.objects()
    }
    if (path == "/") // the Home "View"
      state.value = State("/", emptyList())
    else // the List "View"
      state.value = State(path, items)
  }

  override suspend fun post(
    path: String,
    item: Item
  ) {
    when (path) {
      "/" -> {
        handlePostRoot(item)
      }

      "/lists/new" -> {
        new_list(item)
      }

      else
        -> throw Exception("Unknown path: $path")
    }
  }

  private suspend fun handlePostRoot(item: Item) {
    with(repository) {
      item.save()
    }
    // notifies downstream (UI) ("redirect")
    //state.value = State("/lists/the-only-list-in-the-world/", listOf(item))
    get("/lists/the-only-list-in-the-world/")
  }

  private suspend fun new_list(item: Item) {
    with(repository) {
      item.save()
    }
    get("/lists/the-only-list-in-the-world/")
  }

}