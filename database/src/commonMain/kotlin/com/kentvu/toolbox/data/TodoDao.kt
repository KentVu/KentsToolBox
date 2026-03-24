package com.kentvu.toolbox.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
  @Insert
  suspend fun insert(item: Item)

  @Query("SELECT count(*) FROM Item")
  suspend fun count(): Int

  @Query("SELECT * FROM Item")
  fun getAllAsFlow(): Flow<List<Item>>

  @Query("DELETE FROM Item")
  suspend fun clear():Int
}