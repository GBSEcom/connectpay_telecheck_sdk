package com.firstdata.sdk.sample

import android.app.Application
import com.firstdata.sdk.sample.di.ApplicationComponent
import com.firstdata.sdk.sample.di.ApplicationModule
import com.firstdata.sdk.sample.di.DaggerApplicationComponent
import com.firstdata.sdk.sample.utility.APPLICATION
import com.firstdata.sdk.sample.utility.APP_CONTEXT

class ConnectPayApplication : Application() {

    var appComponent: ApplicationComponent? = null
    override fun onCreate() {
        super.onCreate()

        // Build & Initialize dagger component with supported modules.

        appComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()

        APP_CONTEXT = this
        APPLICATION = this
    }
}