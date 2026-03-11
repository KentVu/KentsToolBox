package com.kentvu.toolbox.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val text: String,
)
