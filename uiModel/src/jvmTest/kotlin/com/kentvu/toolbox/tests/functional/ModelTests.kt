package com.kentvu.toolbox.tests.functional

import com.kentvu.toolbox.DefaultModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.Environment
import com.kentvu.toolbox.client.RemoteDataSource
import com.kentvu.toolbox.data.JvmRoomDatasource
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.State
import com.kentvu.toolbox.FakeRepo
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

// HomePageTests
@OptIn(ExperimentalCoroutinesApi::class)
class ModelTests {
  @Test
  fun test_redirects_after_POST() = runTest {
    // def test_redirects_after_POST(self):
    // response = self.client.post("/", data={"item_text": "A new list item"})
    // self.assertRedirects(response, "/")
    val model = DefaultModel(repository = FakeRepo())
    val states = mutableListOf<State>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
      model.state.toList(states)
    }
    //When
    model.post("/", Item(text = "A new list item"))
    //Then
    states.last().path shouldBe "/lists/the-only-list-in-the-world/"
  }

  @Test
  fun test_can_save_a_POST_request() = runTest {
    val datasource = JvmRoomDatasource(Environment.Test)
    val remoteDataSource = RemoteDataSource()
    val repo = DefaultRepository(remoteDataSource, datasource)
    val model = DefaultModel(repository = repo)
    val states = mutableListOf<State>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
      model.state.toList(states)
    }
    remoteDataSource.itemsClear()

    model.post(
      "/", Item(
        text = "A new list item"
      )
    )
    with(repo) {
      assertEquals(1, remoteDataSource.itemsCount())
      val new_item = Item.objects().first()
      assertEquals("A new list item", new_item.text)
    }

    assertContains(states.last().data, Item("A new list item"))
    assertContains(states.last().path, "/")
  }

  class ListView {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_displays_all_list_items() = runTest {
      val datasource = JvmRoomDatasource(Environment.Test)
      val repo = DefaultRepository(datasource, datasource)
      val model = DefaultModel(repository = repo)
      val states = mutableListOf<State>()
      backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        model.state.toList(states)
      }
      with(repo) {
        Item(text = "itemey 1").save()
        Item(text = "itemey 2").save()
      }

      model.get("/lists/the-only-list-in-the-world/")

      assertContains(states.last().data, Item("itemey 1"))
      assertContains(states.last().data, Item("itemey 2"))
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun test_multiple_users_can_start_lists_at_different_urls() = runTest {
    val datasource = JvmRoomDatasource(Environment.Test)
    val remoteDataSource = RemoteDataSource()
    val repo = DefaultRepository(remoteDataSource, datasource)
    val model = DefaultModel(repository = repo)
    remoteDataSource.itemsClear()
    val states = mutableListOf<State>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
      model.state.toList(states)
    }

    // Edith starts a new to-do list
    model.post("/", Item(text = "Buy peacock feathers"))
    assertContains(states.last().data, Item("Buy peacock feathers"))

    // She notices that her list has a unique URL
    withClue("Current page should has a unique URL") {
      states.last().path shouldMatch Regex("/lists/.+")
    }
    val edith_list_url = states.last().path
    // Now a new user, Francis, comes along to the site.

    //# We delete all the browser's cookies
    //# as a way of simulating a brand-new user session
    //## As REST-API is state-less, no need to reset cookie etc.
    // Francis visits the home page.  There is no sign of Edith's list
    model.get("/")
    states.last().data shouldNotContain Item("Buy peacock feathers")
    // Francis starts a new list by entering a new item. He
    // is less interesting than Edith...
    model.post("/", Item(text = "Buy milk"))
    assertContains(states.last().data, Item("Buy milk"))
    states.last().data.first() shouldBeEqual Item("Buy milk")
    withClue("Francis gets his own unique URL") {
      states.last().path shouldMatch Regex("/lists/.+")
    }
    val francis_list_url = states.last().path
    francis_list_url shouldNotBeEqual edith_list_url
    //Again, there is no trace of Edith's list
    states.last().data shouldNotContain Item("Buy peacock feathers")
    assertContains(states.last().data, Item("Buy milk"))
    //Satisfied, they both go back to sleep
  }

}