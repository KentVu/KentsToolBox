package com.kentvu.toolbox

import com.kentvu.toolbox.models.Item
import io.mockative.Mockable

@Mockable
interface DataSource {
  fun items(): List<Item>

}
