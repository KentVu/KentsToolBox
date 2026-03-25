package com.kentvu.toolbox

import com.kentvu.toolbox.models.FallBackToLocalDataSourceException
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Repository

class DefaultRepository(
  private val primaryDataSource: DataSource,
  private val secondaryDataSource: DataSource
) : Repository {
  override suspend fun Item.Companion.count(): Int {
    return try {
      primaryDataSource.itemsCount()
    } catch (e: Exception) {
      throw FallBackToLocalDataSourceException(secondaryDataSource.itemsCount(), e)
    }
  }

  override suspend fun Item.Companion.objects(): List<Item> {
    return try {
      primaryDataSource.items()
      // TODO cache data
    } catch (e: Exception) {
      throw FallBackToLocalDataSourceException(secondaryDataSource.items(), e)
    }
  }

  override suspend fun Item.save() {
    primaryDataSource.save(this)
  }
}