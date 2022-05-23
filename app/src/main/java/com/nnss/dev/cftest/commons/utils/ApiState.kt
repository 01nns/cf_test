package com.nnss.dev.cftest.commons.utils

data class ApiState<out T>(val status: Status, val data: T?, val msg: String?) {
    constructor(data: T?) : this(Status.IDLE, data, null)

    companion object {
        fun <T> success(data: T?): ApiState<T> {
            return ApiState(Status.SUCCESS, data, null)
        }

        // In case of failure ,set state to Error ,
        // add the error message,set data to null
        fun <T> error(msg: String?): ApiState<T> {
            return ApiState(Status.ERROR, null, msg)
        }

        // When the call is loading set the state
        // as Loading and rest as null
        fun <T> loading(): ApiState<T> {
            return ApiState(Status.LOADING, null, null)
        }
    }
}

enum class Status {
    IDLE,
    SUCCESS,
    ERROR,
    LOADING
}