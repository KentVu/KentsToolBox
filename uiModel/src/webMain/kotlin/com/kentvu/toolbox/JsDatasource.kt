package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import kotlinx.serialization.json.Json
import slack.kotlinlang.localStorage

class JsDatasource: DataSource {
  override suspend fun items(): List<Item> {
    return Json.decodeFromString(localStorage.getItem("items") ?: "[]")
  }

}