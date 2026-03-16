package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

class RemoteDataSource: DataSource {
  override suspend fun items(): List<Item> {
    val client = HttpClient(CIO) {
      install(ContentNegotiation) {
        json()
      }
    }
    TODO()
  }

}
