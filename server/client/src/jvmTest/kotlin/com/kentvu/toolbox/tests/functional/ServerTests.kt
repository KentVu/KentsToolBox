package com.kentvu.toolbox.tests.functional

import com.kentvu.toolbox.client.RemoteDataSource
import com.kentvu.toolbox.models.Item
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContains

class ServerTests {

  @Test
  fun testCanSaveItems() = runTest {
      val remoteDataSource = RemoteDataSource()
      remoteDataSource.save(Item("itemey 1"))
      remoteDataSource.save(Item("itemey 2"))

      val items = remoteDataSource.items()

      assertContains(items, Item("itemey 1"))
      assertContains(items, Item("itemey 2"))
      remoteDataSource.close()
  }

}