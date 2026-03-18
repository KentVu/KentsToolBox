package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.serializable.Item as SItem
import kotlinx.serialization.json.Json
import slack.kotlinlang.localStorage

class JsDatasource: DataSource {
  override suspend fun items(): List<Item> {
    return Json.decodeFromString<List<SItem>>(localStorage.getItem("items") ?: "[]")
      .map { it.toModel() }
  }

  override suspend fun save(item: Item) {
    val items = Json.decodeFromString<List<SItem>>(localStorage.getItem("items") ?: "[]").toMutableList()
    items.add(SItem(item))
    localStorage.setItem("items", Json.encodeToString(items))
  }

}