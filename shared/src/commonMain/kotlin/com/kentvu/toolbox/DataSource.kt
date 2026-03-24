package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item

interface DataSource {
  suspend fun itemsCount(): Int
  suspend fun items(): List<Item>
  suspend fun save(item: Item)
  suspend fun itemsClear(): Int

}