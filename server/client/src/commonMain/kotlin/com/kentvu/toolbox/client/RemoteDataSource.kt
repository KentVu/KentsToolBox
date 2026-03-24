package com.kentvu.toolbox.client

import com.kentvu.toolbox.DataSource
import com.kentvu.toolbox.models.Item
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import com.kentvu.toolbox.models.serializable.Item as SItem

class RemoteDataSource: DataSource, AutoCloseable {
  val client = HttpClient(CIO) {
    install(ContentNegotiation) {
      json()
    }
  }

  override suspend fun itemsCount(): Int {
    val response = client.get("http://127.0.0.1:8080/items/count")
    return response.body()
  }

  override suspend fun items(): List<Item> {
    val response = client.get("http://127.0.0.1:8080/items")
    return response.body<List<SItem>>().map { it.toModel() }
  }

  override suspend fun save(item: Item) {
    val response = client.post("http://127.0.0.1:8080/items") {
      contentType(ContentType.Application.Json)
      setBody(SItem(item))
    }
    // throw if not success
    println("Item save response: " + response.bodyAsText())
    //if (!(response.status.isSuccess())) error("Failed to save Item.")
    require(response.status.isSuccess()) { "Failed to save Item." }
  }

  override fun close() {
    client.close()
  }

  override suspend fun itemsClear(): Int {
    val response = client.get("http://127.0.0.1:8080/items/clear")
    println("Item clear response: " + response.bodyAsText())
    require(response.status.isSuccess()) { "Failed to clear Items." }
    return response.body()
  }

}