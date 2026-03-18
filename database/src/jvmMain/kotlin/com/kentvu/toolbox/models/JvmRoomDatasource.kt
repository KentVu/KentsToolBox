package com.kentvu.toolbox.models

import androidx.room.Room
import com.kentvu.toolbox.DataSource
import com.kentvu.toolbox.Environment
import com.kentvu.toolbox.data.AppDatabase
import com.kentvu.toolbox.data.Item as DaoItem
import com.kentvu.toolbox.data.getDatabaseBuilder
import com.kentvu.toolbox.data.getRoomDatabase
import kotlinx.coroutines.flow.first

/** Beware the parameter [db] default value uses **[Room.inMemoryDatabaseBuilder]** */
class JvmRoomDatasource(
  environment: Environment,
  val db: AppDatabase = getRoomDatabase(
    if(environment == Environment.Production) getDatabaseBuilder()
    else Room.inMemoryDatabaseBuilder()
  )
) : DataSource {
  suspend fun Item.Companion.count(): Int =
    db.getDao().count()

  override suspend fun save(item: Item) {
    db.getDao().insert(DaoItem(text = item.text))
  }

  private fun List<DaoItem>.toModel(): List<Item> = map {
    Item(text = it.text)
  }

  override suspend fun items(): List<Item> {
    return db.getDao().getAllAsFlow().first().toModel()
  }
}
