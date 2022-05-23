package com.nnss.dev.cftest

import android.app.Application
import com.nnss.dev.cftest.data.data
import com.nnss.dev.cftest.commons.commons
import com.nnss.dev.cftest.domain.repository
import com.nnss.dev.cftest.domain.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {
    init {
        System.loadLibrary("native-lib")
    }
    private external fun baseUrl(): String

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)
            androidContext(this@App)
            androidFileProperties()
            modules(
                commons(),
                data(baseUrl(), BuildConfig.DEBUG),
                repository(),
                viewModel()
            )
        }
    }
}