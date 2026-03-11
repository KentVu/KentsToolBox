package com.kentvu.toolbox.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ItemModelRoomTest {
  @Test
  fun test_saving_and_retrieving_items() = runTest {
    val db = getRoomDatabase(getDatabaseBuilder())
    val dao = db.getDao()

    val first_item = Item(text = "The first (ever) list item")
    dao.insert(first_item)

    val second_item = Item(text = "Item the second")
    dao.insert(second_item)

    val saved_items = dao.getAllAsFlow().first()
    assertEquals(2, saved_items.size)

    val first_saved_item = saved_items[0]
    val second_saved_item = saved_items[1]
    assertEquals("The first (ever) list item", first_saved_item.text)
    assertEquals("Item the second", second_saved_item.text)
  }
}