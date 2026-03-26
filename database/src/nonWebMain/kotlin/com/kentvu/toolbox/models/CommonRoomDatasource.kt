package com.kentvu.toolbox.models

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kentvu.toolbox.DataSource
import com.kentvu.toolbox.Environment
import com.kentvu.toolbox.data.AppDatabase
import com.kentvu.toolbox.data.Item as DaoItem
import com.kentvu.toolbox.data.getRoomDatabase
import kotlinx.coroutines.flow.first

/** Beware the parameter [db] default value uses **[Room.inMemoryDatabaseBuilder]** */
class CommonRoomDatasource(
  dbBuilder: RoomDatabase.Builder<AppDatabase>// = Room.inMemoryDatabaseBuilder()
) : DataSource {

  val db = getRoomDatabase(dbBuilder)
  override suspend fun itemsCount(): Int =
    db.getDao().count()

  override suspend fun save(item: Item) {
    db.getDao().insert(DaoItem(text = item.text))
  }

  override suspend fun itemsClear(): Int {
    val count = db.getDao().clear()
    return count
  }

  private fun List<DaoItem>.toModel(): List<Item> = map {
    Item(text = it.text)
  }

  override suspend fun items(): List<Item> {
    return db.getDao().getAllAsFlow().first().toModel()
  }
}
