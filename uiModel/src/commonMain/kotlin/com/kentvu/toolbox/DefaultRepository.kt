package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Repository

class DefaultRepository(localDataSource: DataSource, remoteDataSource: DataSource) : Repository {
  override suspend fun Item.Companion.count(): Int {
    TODO("Not yet implemented")
  }

  override suspend fun Item.Companion.objects(): List<Item> {
    TODO("Not yet implemented")
  }

  override suspend fun Item.save() {
    TODO("Not yet implemented")
  }
}