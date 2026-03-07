package com.kentvu.toolbox

import com.kentvu.toolbox.Backend.Action
import com.kentvu.toolbox.models.Item
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class BackendTests {
  @Test
  fun test_can_save_a_POST_request() {
    val backend = Backend()
    val response = backend.post(
      Action.NewItem, Item(
        item_text = "A new list item"
      )
    )
    assertContains(response/*.model*/.items, Item("A new list item"))
  }
}