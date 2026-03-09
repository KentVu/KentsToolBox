package com.kentvu.toolbox.tests

import com.kentvu.toolbox.Backend
import com.kentvu.toolbox.Backend.Action
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Model
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContains

class BackendTests {
  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun test_can_save_a_POST_request() = runTest {
    val backend = Backend.Default()
    val models = mutableListOf<Model>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        backend.model.toList(models)
    }
    backend.post(
      Action.Add, Item(
        item_text = "A new list item"
      )
    )
    assertContains(models.last().items, Item("A new list item"))
  }
}