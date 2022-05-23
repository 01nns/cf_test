package com.nnss.dev.cftest.commons.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.nnss.dev.cftest.data.remote.model.GenericResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

fun <T> toResultFlow(call: suspend () -> Response<T>?): Flow<ApiState<T>> {
    return flow {
        emit(ApiState.loading())

        try {
            val c = call()
            c?.let {
                kotlinx.coroutines.delay(1500)
                if (c.isSuccessful) {
                    emit(ApiState.success(c.body()))
                } else {
                    c.errorBody()?.let { errBody ->
                        val gson = GsonBuilder().create()
                        val resp: GenericResponse?
                        var msg = ""

                        try {
                            resp = gson.fromJson(errBody.string(), GenericResponse::class.java)
                            msg = resp?.message ?: COMMON_ERROR

                        } catch (e: IOException) {
                            e.message?.let { Timber.e(it) }
                        }
                        emit(ApiState.error<Nothing>(msg))
                    }
                }
            }
        } catch (e: Exception) {
            emit(ApiState.error<Nothing>(e.message))
        }

    }.flowOn(Dispatchers.IO)
}

inline fun Fragment.observeFlows(crossinline observationFunction: suspend (CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycle.coroutineScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            observationFunction(this)
        }
    }
}

inline fun <T> Flow<T>.collectLA(
    owner: LifecycleOwner,
    crossinline onCollect: suspend (T) -> Unit
) = owner.lifecycleScope.launch { owner.repeatOnLifecycle(Lifecycle.State.STARTED) { collect { onCollect(it) } } }

inline fun <T> Flow<T>.collectLatestLA(
    owner: LifecycleOwner,
    crossinline onCollect: suspend (T) -> Unit
) = owner.lifecycleScope.launch { owner.repeatOnLifecycle(Lifecycle.State.STARTED) { collectLatest { onCollect(it) } } }
