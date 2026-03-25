package com.kentvu.toolbox.tests

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Repository

class FakeRepo: Repository {
  override suspend fun Item.Companion.count(): Int {
    TODO("Not yet implemented")
  }

  override suspend fun Item.Companion.objects(): List<Item> {
    TODO("Not yet implemented")
  }

  override suspend fun Item.save() {
    println("To be saved: $this")
  }

}