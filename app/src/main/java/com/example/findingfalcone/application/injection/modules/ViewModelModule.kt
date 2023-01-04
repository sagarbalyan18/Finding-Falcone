package com.example.findingfalcone.application.injection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun bindViewModelFactory(providers: MutableMap<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                        requireNotNull(providers[modelClass]).get() as T
            }
}