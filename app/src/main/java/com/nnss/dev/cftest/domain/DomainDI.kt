package com.nnss.dev.cftest.domain

import com.nnss.dev.cftest.data.local.roomdb.AppDatabase
import com.nnss.dev.cftest.data.remote.Api
import com.nnss.dev.cftest.domain.repository.DbRepository
import com.nnss.dev.cftest.domain.repository.DbRepositoryImpl
import com.nnss.dev.cftest.domain.repository.MainRepository
import com.nnss.dev.cftest.domain.repository.MainRepositoryImpl
import com.nnss.dev.cftest.ui.calculator.CalculatorViewModel
import com.nnss.dev.cftest.ui.home.HomeViewModel
import com.nnss.dev.cftest.ui.onboard.OnboardViewModel
import com.nnss.dev.cftest.ui.post.PostViewModel
import com.nnss.dev.cftest.ui.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun repository() = module {
    fun provideMainRepository(api: Api) : MainRepository {
        return MainRepositoryImpl(api)
    }

    fun provideDbRepository(appDatabase: AppDatabase) : DbRepository {
        return DbRepositoryImpl(appDatabase)
    }

    single { provideMainRepository(get()) }

    single { provideDbRepository(get()) }
}

fun viewModel() = module {
    viewModel { CalculatorViewModel(get()) }
    viewModel { OnboardViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { PostViewModel(get(), get()) }
    viewModel { UserViewModel(get(), get()) }
}