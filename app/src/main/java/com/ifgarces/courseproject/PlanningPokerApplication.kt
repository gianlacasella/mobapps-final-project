package com.ifgarces.courseproject

import android.app.Application
import com.ifgarces.courseproject.Modules.appModule
import com.ifgarces.courseproject.Modules.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlanningPokerApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@PlanningPokerApplication)
            modules(listOf(appModule, networkModule))
        }
    }
}