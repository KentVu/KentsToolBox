package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item

interface DataSource {
  suspend fun items(): List<Item>

}