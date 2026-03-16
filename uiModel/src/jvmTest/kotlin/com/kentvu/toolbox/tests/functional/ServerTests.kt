package com.kentvu.toolbox.tests.functional

import com.kentvu.toolbox.models.Item
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import java.net.ConnectException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ServerTests {

  @Test
  fun getItems() = runTest {
    val client = HttpClient(CIO) {
      install(ContentNegotiation) {
        json()
      }
    }

    val response = try {
      client.get("http://127.0.0.1:8080/items")
    } catch (e: ConnectException) {
      fail("Server is not running, please run server first!", e)
    }
    print(response)
    assertTrue(response.status.isSuccess(), "response not success!")
    println(" body: " + response.bodyAsText())
    val items = response.body<List<Item>>()
    println(" body: $items")
    client.close()
  }

}