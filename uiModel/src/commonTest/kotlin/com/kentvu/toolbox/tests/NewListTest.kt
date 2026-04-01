package com.kentvu.toolbox.tests

import com.kentvu.toolbox.DefaultModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.FakeRepo
import com.kentvu.toolbox.data.InMemDataSource
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NewListTest {
  @Test
  fun test_can_save_a_POST_request() = runTest {
    val fakeDataSource = InMemDataSource()
    val repo = DefaultRepository(fakeDataSource, InMemDataSource())
    val model = DefaultModel(repo)
    model.post("/lists/new", Item(text = "A new list item"))
    with(repo) {
      val objects = Item.objects()
      assertEquals(1, objects.count())
      assertEquals("A new list item", objects.first().text)
    }
  }

  @Test
  fun test_redirects_after_POST() = runTest {
    // def test_redirects_after_POST(self):
    // response = self.client.post("/", data={"item_text": "A new list item"})
    // self.assertRedirects(response, "/")
    val model = DefaultModel(repository = DefaultRepository(InMemDataSource(), InMemDataSource()))
    val states = mutableListOf<State>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
      model.state.toList(states)
    }
    //When
    model.post("/lists/new", Item(text = "A new list item"))
    //Then
    assertEquals("/lists/the-only-list-in-the-world/", states.last().path)
  }

}