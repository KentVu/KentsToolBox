package com.kentvu.toolbox

import com.kentvu.toolbox.models.FallBackToLocalDataSourceException
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Repository

class DefaultRepository(private val localDataSource: DataSource, private val remoteDataSource: DataSource) : Repository {
  override suspend fun Item.Companion.count(): Int {
    return try {
      remoteDataSource.itemsCount()
    } catch (e: Exception) {
      throw FallBackToLocalDataSourceException(localDataSource.itemsCount(), e)
    }
  }

  override suspend fun Item.Companion.objects(): List<Item> {
    return try {
      remoteDataSource.items()
      // TODO cache data
    } catch (e: Exception) {
      throw FallBackToLocalDataSourceException(localDataSource.items(), e)
    }
  }

  override suspend fun Item.save() {
    remoteDataSource.save(this)
  }
}