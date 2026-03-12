package com.kentvu.toolbox.models

import androidx.room.Room
import com.kentvu.toolbox.Enviroment
import com.kentvu.toolbox.data.AppDatabase
import com.kentvu.toolbox.data.getDatabaseBuilder
import com.kentvu.toolbox.data.getRoomDatabase
import kotlinx.coroutines.flow.first

/** Beware the parameter [db] default value uses **[Room.inMemoryDatabaseBuilder]** */
class JvmRoomRepository(
  enviroment: Enviroment,
  val db: AppDatabase = getRoomDatabase(
    if(enviroment == Enviroment.Production) getDatabaseBuilder()
    else Room.inMemoryDatabaseBuilder()
  )
) : Repository {
  override suspend fun Item.Companion.count(): Int =
    db.getDao().count()

  override suspend fun Item.Companion.objects(): List<Item> =
    db.getDao().getAllAsFlow().first().toModel()

  override suspend fun Item.save() {
    db.getDao().insert(com.kentvu.toolbox.data.Item(text = text))
  }

  private fun List<com.kentvu.toolbox.data.Item>.toModel(): List<Item> = map {
    Item(text = it.text)
  }
}
