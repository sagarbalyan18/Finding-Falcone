package com.example.findingfalcone.application.injection.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.findingfalcone.application.LocalProperties
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    companion object {
        private const val MY_PREF = "MyPref"
    }

    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideSharedPreference(): SharedPreferences =
        application.applicationContext.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideLocaleProperties(sharedPreferences: SharedPreferences) = LocalProperties(sharedPreferences)
}