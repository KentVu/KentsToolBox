package com.kentvu.toolbox.tests

import com.kentvu.toolbox.DataSource
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.models.FallBackToLocalDataSourceException
import com.kentvu.toolbox.models.Item
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.collections.listOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DefaultRepositoryTest {

  @Test
  fun fallBackToLocalDataSource() = runTest {
    val localDataSource = mockk<DataSource>()
    val remoteDataSource = mockk<DataSource>()
    val expected = listOf(Item("item 1"))
    coEvery { localDataSource.items() }.returns(expected)
    coEvery { remoteDataSource.items() }.throws(Exception())
    val repository = DefaultRepository(localDataSource, remoteDataSource)
    //var exception: Exception? = null
    //var result = Result.success(listOf<Item>())

    val result = repository.runCatching {
      Item.objects()
    }

    assertIs<FallBackToLocalDataSourceException>(result.exceptionOrNull(), "Expected DataSourceException")
    assertEquals(expected, (result.exceptionOrNull() as FallBackToLocalDataSourceException).data)
  }
}