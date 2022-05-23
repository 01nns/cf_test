package com.nnss.dev.cftest.ui.onboard

import android.content.SharedPreferences
import com.nnss.dev.cftest.commons.base.BaseViewModel
import com.nnss.dev.cftest.commons.utils.ApiState
import com.nnss.dev.cftest.commons.utils.PreferenceHelper.set
import com.nnss.dev.cftest.commons.utils.TOKEN
import com.nnss.dev.cftest.commons.utils.USERNAME
import com.nnss.dev.cftest.data.remote.model.CreateUserRequest
import com.nnss.dev.cftest.data.remote.model.GenericResponse
import com.nnss.dev.cftest.data.remote.model.LoginRequest
import com.nnss.dev.cftest.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OnboardViewModel(private val repository: MainRepository, private val prefs: SharedPreferences) :
    BaseViewModel() {

    val state = MutableStateFlow(ApiState(GenericResponse()))

    fun login(request: LoginRequest) = launch {
        repository.login(request)
            .collect {
                state.value = it
            }
    }

    fun saveData(token: String?, username: String?) {
        prefs[TOKEN] = token
        prefs[USERNAME] = username
    }

    fun createUser(request: CreateUserRequest)= launch {
        repository.register(request)
            .collect {
                state.value = it
            }
    }

}