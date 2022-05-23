package com.nnss.dev.cftest.domain.repository

import com.nnss.dev.cftest.data.local.model.CalcInput
import com.nnss.dev.cftest.data.local.roomdb.AppDatabase
import kotlinx.coroutines.flow.Flow


interface DbRepository {
    suspend fun getAll() : Flow<MutableList<CalcInput>>
    suspend fun insert(calcInput: CalcInput)
}

class DbRepositoryImpl(private val appDatabase: AppDatabase) : DbRepository {
    override suspend fun getAll(): Flow<MutableList<CalcInput>> {
        return appDatabase.CalcDao().getAll()
    }

    override suspend fun insert(calcInput: CalcInput) {
        appDatabase.CalcDao().insert(calcInput)
    }
}