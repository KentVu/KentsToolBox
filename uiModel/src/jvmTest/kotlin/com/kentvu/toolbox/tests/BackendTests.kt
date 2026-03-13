package com.kentvu.toolbox.tests

import com.kentvu.toolbox.BackendJvm
import com.kentvu.toolbox.Environment
import com.kentvu.toolbox.models.JvmRoomRepository
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Model
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

// HomePageTests
class BackendTests {
  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun test_can_save_a_POST_request() = runTest {
    val repo = JvmRoomRepository(Environment.Dev)
    val backend = BackendJvm(repository = repo)
    val models = mutableListOf<Model>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
      backend.model.toList(models)
    }

    backend.post(
      "/", Item(
        text = "A new list item"
      )
    )
    with(repo) {
      assertEquals(1, Item.count())
      val new_item = Item.objects().first()
      assertEquals( "A new list item", new_item.text)
    }

    assertContains(models.last().data, Item("A new list item"))
    assertEquals( "/", models.last().path)
  }

  @Test
  fun test_displays_all_list_items() = runTest {
    val repo = JvmRoomRepository(Environment.Dev)
    val backend = BackendJvm(repository = repo)
    val models = mutableListOf<Model>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
      backend.model.toList(models)
    }
    with(repo) {
      Item(text = "itemey 1").save()
      Item(text = "itemey 2").save()
    }

    backend.get("/")

    assertContains(models.last().data, Item("itemey 1"))
    assertContains(models.last().data, Item("itemey 2"))
  }
}