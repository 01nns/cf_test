package com.nnss.dev.cftest.ui.home

import android.content.SharedPreferences
import com.nnss.dev.cftest.commons.base.BaseViewModel
import com.nnss.dev.cftest.commons.utils.ApiState
import com.nnss.dev.cftest.data.remote.model.FeedResponse
import com.nnss.dev.cftest.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository, private val prefs: SharedPreferences) :
    BaseViewModel() {

    val state = MutableStateFlow(ApiState(listOf<FeedResponse>()))

   fun getFeed() = launch {
       repository.getFeed()
           .collect {
               state.value = it
           }
   }

}