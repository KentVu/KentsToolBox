package com.kentvu.toolbox.data

import com.kentvu.toolbox.DataSource
import com.kentvu.toolbox.models.Item

class InMemDataSource: DataSource {
  val items = mutableListOf<Item>()
  override suspend fun itemsCount(): Int {
    return items.count()
  }

  override suspend fun items(): List<Item> {
    return items
  }

  override suspend fun save(item: Item) {
    items.add(item)
  }

  override suspend fun itemsClear(): Int {
    //val save = items.count(); items.clear(); return save
    return items.count().also { items.clear() }
  }
}