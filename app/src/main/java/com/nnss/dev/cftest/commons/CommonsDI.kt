package com.nnss.dev.cftest.commons

import com.nnss.dev.cftest.commons.utils.PreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun commons() = module {

    single { PreferenceHelper.customPrefs(androidContext(), "bfta") }
}