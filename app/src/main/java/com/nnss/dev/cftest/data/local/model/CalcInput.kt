package com.nnss.dev.cftest.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calc_inputs")
data class CalcInput(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "inputs") val inputs: String
) {
    constructor() : this(0, "")
}