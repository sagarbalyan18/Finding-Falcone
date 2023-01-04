package com.example.findingfalcone.application.injection.components

import androidx.lifecycle.ViewModelProvider
import com.example.findingfalcone.application.MyApp
import com.example.findingfalcone.application.injection.modules.ApplicationModule
import com.example.findingfalcone.application.injection.modules.MainModule
import com.example.findingfalcone.application.injection.modules.NetworkModule
import com.example.findingfalcone.application.injection.modules.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        MainModule::class,
        ViewModelModule::class,
        NetworkModule::class]
)
interface AppComponent {

    fun inject(app: MyApp)

    // ViewModel factory
    fun viewModelFactory(): ViewModelProvider.Factory
}