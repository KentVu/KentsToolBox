package com.kentvu.toolbox.tests.functional

import com.kentvu.toolbox.DefaultModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.Environment
import com.kentvu.toolbox.client.RemoteDataSource
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.JvmRoomDatasource
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

// HomePageTests
class ModelTests {
  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun test_can_save_a_POST_request() = runTest {
    val datasource = JvmRoomDatasource(Environment.Test)
    val repo = DefaultRepository(datasource, RemoteDataSource())
    val model = DefaultModel(repository = repo)
    val states = mutableListOf<State>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
      model.state.toList(states)
    }

    model.post(
      "/", Item(
        text = "A new list item"
      )
    )
    with(repo) {
      assertEquals(1, Item.count())
      val new_item = Item.objects().first()
      assertEquals("A new list item", new_item.text)
    }

    assertContains(states.last().data, Item("A new list item"))
    assertEquals("/", states.last().path)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun test_displays_all_list_items() = runTest {
    val datasource = JvmRoomDatasource(Environment.Test)
    val repo = DefaultRepository(datasource, datasource)
    val backend = DefaultModel(repository = repo)
    val states = mutableListOf<State>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
      backend.state.toList(states)
    }
    with(repo) {
      Item(text = "itemey 1").save()
      Item(text = "itemey 2").save()
    }

    backend.get("/")

    assertContains(states.last().data, Item("itemey 1"))
    assertContains(states.last().data, Item("itemey 2"))
  }

}