package com.nnss.dev.cftest.ui.post

import android.content.SharedPreferences
import com.nnss.dev.cftest.commons.base.BaseViewModel
import com.nnss.dev.cftest.commons.utils.ApiState
import com.nnss.dev.cftest.data.remote.model.*
import com.nnss.dev.cftest.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val repository: MainRepository, private val prefs: SharedPreferences) :
    BaseViewModel() {

    val state = MutableStateFlow(ApiState(FeedResponse()))
    val commentState = MutableStateFlow(ApiState(FeedResponse()))

   fun newPost(request: NewPostRequest) = launch {
       repository.newPost(request)
           .collect {
               state.value = it
           }
   }

    fun newComment(id: String?, request: NewCommentRequest) = launch {
        repository.newComment(id, request)
            .collect {
                commentState.value = it
            }
    }
}