package com.nnss.dev.cftest.ui.calculator

import com.nnss.dev.cftest.commons.base.BaseViewModel
import com.nnss.dev.cftest.data.local.model.CalcInput
import com.nnss.dev.cftest.domain.repository.DbRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CalculatorViewModel(private val repository: DbRepository) : BaseViewModel() {

    val state = MutableStateFlow(listOf(CalcInput()))

    fun saveInput(value: String) = launch {
        repository.insert(CalcInput(0,value))
    }

    fun getInputs() = launch {
        repository.getAll()
            .collect {
                state.value = it
            }
    }
}