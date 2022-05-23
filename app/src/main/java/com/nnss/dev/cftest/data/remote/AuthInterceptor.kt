package com.nnss.dev.cftest.data.remote

import android.content.SharedPreferences
import com.nnss.dev.cftest.commons.utils.TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val prefs: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        prefs.getString(TOKEN, "")?.let { requestBuilder.addHeader("Authorization", it) }

        return chain.proceed(requestBuilder.build())
    }

}