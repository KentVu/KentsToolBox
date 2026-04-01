package com.kentvu.toolbox.tests

import androidx.compose.runtime.mutableStateListOf
import com.kentvu.toolbox.DefaultHttpModel
import com.kentvu.toolbox.DefaultRepository
import com.kentvu.toolbox.HttpModel
import com.kentvu.toolbox.models.Item
import com.kentvu.toolbox.models.Repository
import com.kentvu.toolbox.models.State

class SpyHttpModel(private val m: HttpModel) : HttpModel by m {
  constructor(repo: Repository) : this(DefaultHttpModel(repo))
  var calledPostPath: String? = null
  val calledPost: Boolean get() = calledPostPath != null
  var calledGetTimes: Int = 0
  val backstack = mutableStateListOf("/")

  override suspend fun post(path: String, item: Item): State {
    calledPostPath = path
    return m.post(path, item)
  }

  override suspend fun get(path: String): State {
    calledGetTimes++
    return m.get(path)
  }

}