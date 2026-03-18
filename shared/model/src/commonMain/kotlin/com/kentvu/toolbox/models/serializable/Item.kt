package com.kentvu.toolbox.models.serializable

import com.kentvu.toolbox.models.Item as MItem
import kotlinx.serialization.Serializable

@Serializable
data class Item(val text: String) {
  constructor(mi: MItem) : this(mi.text)

  fun toModel(): MItem = MItem(text)
}
