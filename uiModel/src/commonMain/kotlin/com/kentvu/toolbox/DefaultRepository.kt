package com.kentvu.toolbox

import com.kentvu.toolbox.models.FallBackToLocalDataSourceException
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Repository

class DefaultRepository(private val localDataSource: DataSource, private val remoteDataSource: DataSource) : Repository {
  override suspend fun Item.Companion.count(): Int {
    TODO("Not yet implemented")
  }

  override suspend fun Item.Companion.objects(): List<Item> {
    return try {
      remoteDataSource.items()
    } catch (e: Exception) {
      throw FallBackToLocalDataSourceException(localDataSource.items(), e)
    }
  }

  override suspend fun Item.save() {
    TODO("Not yet implemented")
  }
}