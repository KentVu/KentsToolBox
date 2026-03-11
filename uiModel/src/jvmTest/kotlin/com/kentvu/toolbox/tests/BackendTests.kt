package com.kentvu.toolbox.tests

import com.kentvu.toolbox.Backend
import com.kentvu.toolbox.BackendJvm
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

class BackendTests {
  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun test_can_save_a_POST_request() = runTest {
    val repo = JvmRoomRepository()
    val backend = BackendJvm(repo/*,CoroutineScope(currentCoroutineContext())*/)
    val models = mutableListOf<Model>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
      backend.model.toList(models)
    }

    backend.post(
      Backend.Action.Add, Item(
        text = "A new list item"
      )
    )
    with(repo) {
      assertEquals(1, Item.count())
      val new_item = Item.objects().first()
      assertEquals( "A new list item", new_item.text)
    }

    assertContains(models.last().items, Item("A new list item"))
  }
}