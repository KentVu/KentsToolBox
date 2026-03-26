package com.kentvu.toolbox.tests

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Repository

class FakeRepo: Repository {
  val items = mutableListOf<Item>()
  override suspend fun Item.Companion.count(): Int {
    return items.count()
  }

  override suspend fun Item.Companion.objects(): List<Item> {
    return items
  }

  override suspend fun Item.save() {
    items.add(this)
  }

}