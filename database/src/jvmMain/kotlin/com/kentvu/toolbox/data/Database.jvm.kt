package com.kentvu.toolbox.data

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kentvu.toolbox.Environment
import com.kentvu.toolbox.models.CommonRoomDatasource
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    // TODO database location
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}

fun JvmRoomDatasource(environment: Environment): CommonRoomDatasource {
    val dbBuilder: RoomDatabase.Builder<AppDatabase> =
        if(environment == Environment.Production) getDatabaseBuilder()
        else Room.inMemoryDatabaseBuilder()
    return CommonRoomDatasource(dbBuilder)
}

