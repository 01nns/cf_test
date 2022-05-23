package com.nnss.dev.cftest.domain.repository

import com.nnss.dev.cftest.commons.utils.ApiState
import com.nnss.dev.cftest.commons.utils.toResultFlow
import com.nnss.dev.cftest.data.remote.Api
import com.nnss.dev.cftest.data.remote.model.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface MainRepository {
    fun login(request: LoginRequest?) : Flow<ApiState<GenericResponse>>
    fun register(request: CreateUserRequest?) : Flow<ApiState<GenericResponse>>
    fun getFeed() : Flow<ApiState<List<FeedResponse>>>
    fun newPost(request: NewPostRequest?) : Flow<ApiState<FeedResponse>>
    fun newComment(id: String?, request: NewCommentRequest?) : Flow<ApiState<FeedResponse>>
    fun getUser(user: String) : Flow<ApiState<UserInfo>>
    fun updateProfile(body: MultipartBody) : Flow<ApiState<GenericResponse>>
    fun logout() : Flow<ApiState<GenericResponse>>
}

class MainRepositoryImpl(private val api: Api) : MainRepository {
    override fun login(request: LoginRequest?): Flow<ApiState<GenericResponse>> {
        return toResultFlow {
            api.login(request)
        }
    }

    override fun register(request: CreateUserRequest?): Flow<ApiState<GenericResponse>> {
        return toResultFlow {
            api.createUser(request)
        }
    }

    override fun getFeed(): Flow<ApiState<List<FeedResponse>>> {
        return toResultFlow {
            api.getFeed()
        }
    }

    override fun newPost(request: NewPostRequest?): Flow<ApiState<FeedResponse>> {
        return toResultFlow {
            api.newPost(request)
        }
    }

    override fun newComment(id: String?, request: NewCommentRequest?): Flow<ApiState<FeedResponse>> {
        return toResultFlow {
            api.newComment(id, request)
        }
    }

    override fun getUser(user: String): Flow<ApiState<UserInfo>> {
        return toResultFlow {
            api.getUser(user)
        }
    }

    override fun updateProfile(body: MultipartBody): Flow<ApiState<GenericResponse>> {
        return toResultFlow {
            api.uploadImage(body)
        }
    }

    override fun logout(): Flow<ApiState<GenericResponse>> {
        return toResultFlow {
            api.logout()
        }
    }


}