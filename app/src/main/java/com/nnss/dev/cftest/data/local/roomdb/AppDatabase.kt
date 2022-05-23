package com.nnss.dev.cftest.data.local.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nnss.dev.cftest.data.local.model.CalcInput

@Database(entities = [CalcInput::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CalcDao() : CalcDao
}