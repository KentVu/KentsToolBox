package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item

interface DataSource {
  fun items(): List<Item>

}
