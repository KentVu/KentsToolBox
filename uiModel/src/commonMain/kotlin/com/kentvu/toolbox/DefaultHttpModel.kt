package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Repository
import com.kentvu.toolbox.models.State

class DefaultHttpModel(
  private val repository: Repository,
) : HttpModel {

  override suspend fun get(path: String): State {
    val items = with(repository) {
      Item.objects()
    }
    return when (path) {
      "/" // the Home "View"
        -> State("/", emptyList())

      "/lists/the-only-list-in-the-world/" // the List "View"
        -> State(path, items)

      else -> throw Exception("GET: Unknown path: $path")
    }
  }

  override suspend fun post(
    path: String,
    item: Item
  ): State {
    when (path) {
      "/" -> {
        return home_page_post(item)
      }

      "/lists/new" -> {
        return new_list(item)
      }

      else
        -> throw Exception("POST: Unknown path: $path")
    }
  }

  private suspend fun home_page_post(item: Item): State {
    // notifies downstream (UI) ("redirect")
    //state.value = State("/lists/the-only-list-in-the-world/", listOf(item))
    return get("/lists/the-only-list-in-the-world/")
  }

  private suspend fun new_list(item: Item): State {
    with(repository) {
      item.save()
    }
    return get("/lists/the-only-list-in-the-world/")
  }

}