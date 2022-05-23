package com.nnss.dev.cftest.data

import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.nnss.dev.cftest.data.local.roomdb.AppDatabase
import com.nnss.dev.cftest.data.remote.Api
import com.nnss.dev.cftest.data.remote.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Nirson Nino Samson 05/08/2022.
 *
 * Data Module Injection
 */

fun data(baseUrl: String, isDebug: Boolean) = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .followSslRedirects(true)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (isDebug)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            })
            .addInterceptor(AuthInterceptor(get()))
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory { get<Retrofit>().create(Api::class.java) }

    single { Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "bfta").build()}

    single { get<AppDatabase>().CalcDao() }
}