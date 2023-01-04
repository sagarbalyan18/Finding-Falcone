package com.example.findingfalcone.presentation.vehicleselection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findingfalcone.application.extensions.SingleLiveEvent
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.domain.model.Vehicle
import javax.inject.Inject

class VehicleSelectionViewModel @Inject constructor() : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val _navigation = SingleLiveEvent<NavigationEvent>()
    val navigation: LiveData<NavigationEvent> = _navigation

    private var initialised = false
    lateinit var planet: Planet

    fun init(planet: Planet, availableVehicle: ArrayList<Vehicle>) {
        if (!initialised) {
            initialised = true

            this.planet = planet
            if (availableVehicle.isNotEmpty()) {
                _viewState.value = ViewState(availableVehicle)
            }
        }
    }

    fun onItemClicked(vehicle: Vehicle) {
        _navigation.value = NavigationEvent(planet, vehicle)
    }

    data class ViewState(val vehicles: ArrayList<Vehicle>)
    data class NavigationEvent(val planet: Planet, val vehicle: Vehicle)
}