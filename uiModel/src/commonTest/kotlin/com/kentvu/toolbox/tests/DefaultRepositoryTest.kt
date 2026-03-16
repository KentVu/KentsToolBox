package com.kentvu.toolbox.tests

import com.kentvu.toolbox.DataSource
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.models.Item
import io.mockative.every
import io.mockative.mock
import io.mockative.of
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultRepositoryTest {

  private val localDataSource = mock(of<DataSource>())
  private val remoteDataSource = mock(of<DataSource>())

  @Test
  fun fallBackToLocalDataSource() = runTest {
    val expected = listOf(Item("item 1"))
    every { localDataSource.items() }.returns(expected)
    every { remoteDataSource.items() }.throws(Exception())
    val repository = DefaultRepository(localDataSource, remoteDataSource)

    val items = with(repository) {
      Item.objects()
    }

    assertEquals(expected, items)
  }
}