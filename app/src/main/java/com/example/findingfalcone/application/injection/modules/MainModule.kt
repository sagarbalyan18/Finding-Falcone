package com.example.findingfalcone.application.injection.modules

import androidx.lifecycle.ViewModel
import com.example.findingfalcone.application.injection.ViewModelKey
import com.example.findingfalcone.data.ServiceManager
import com.example.findingfalcone.domain.Repository
import com.example.findingfalcone.presentation.MainActivity
import com.example.findingfalcone.presentation.MainViewModel
import com.example.findingfalcone.presentation.find.FindActivity
import com.example.findingfalcone.presentation.find.FindViewModel
import com.example.findingfalcone.presentation.vehicleselection.VehicleSelectionActivity
import com.example.findingfalcone.presentation.vehicleselection.VehicleSelectionViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @Binds
    abstract fun bindRepository(manager: ServiceManager): Repository

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun bindVehicleSelectionActivity(): VehicleSelectionActivity

    @Binds
    @IntoMap
    @ViewModelKey(VehicleSelectionViewModel::class)
    abstract fun bindVehicleSelectionViewModel(viewModel: VehicleSelectionViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun bindFindActivity(): FindActivity

    @Binds
    @IntoMap
    @ViewModelKey(FindViewModel::class)
    abstract fun bindFindViewModel(viewModel: FindViewModel): ViewModel
}