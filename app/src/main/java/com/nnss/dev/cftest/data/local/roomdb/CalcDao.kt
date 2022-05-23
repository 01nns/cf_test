package com.nnss.dev.cftest.data.local.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nnss.dev.cftest.data.local.model.CalcInput

@Dao
interface CalcDao {
    @Query("SELECT * from calc_inputs")
    fun getAll() : kotlinx.coroutines.flow.Flow<MutableList<CalcInput>>

    @Insert
    suspend fun insert(calcInput: CalcInput)
}