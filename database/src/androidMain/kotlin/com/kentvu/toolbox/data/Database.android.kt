package com.kentvu.toolbox.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kentvu.toolbox.models.CommonRoomDatasource

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
  val appContext = context.applicationContext
  val dbFile = appContext.getDatabasePath("my_room.db")
  return Room.databaseBuilder<AppDatabase>(
    context = appContext,
    name = dbFile.absolutePath
  )
}

fun AndroidRoomDatasource(context: Context): CommonRoomDatasource {
  val dbBuilder: RoomDatabase.Builder<AppDatabase> = getDatabaseBuilder(context)
  return CommonRoomDatasource(dbBuilder)
}
