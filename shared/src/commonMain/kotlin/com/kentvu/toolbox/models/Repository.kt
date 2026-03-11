package com.kentvu.toolbox.models

interface Repository {

  suspend fun Item.Companion.count(): Int

  suspend fun Item.Companion.objects(): List<Item>

  suspend fun Item.save()

}