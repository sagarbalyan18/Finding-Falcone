package com.example.findingfalcone.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findingfalcone.application.extensions.SingleLiveEvent
import com.example.findingfalcone.domain.Repository
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.domain.model.Vehicle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private companion object {
        const val TAG = "MAIN_VIEW_MODEL"
    }

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val _navigation = SingleLiveEvent<NavigationEvent>()
    val navigation: LiveData<NavigationEvent> = _navigation

    private val _loading = SingleLiveEvent<Loading>()
    val loading: LiveData<Loading> = _loading

    private val _message = SingleLiveEvent<String>()
    val message: LiveData<String> = _message

    private var initialised = false
    private var disposable = CompositeDisposable()

    private lateinit var planets: List<Planet>
    private lateinit var vehicles: List<Vehicle>

    private val selectedItems = mutableMapOf<Planet, Vehicle?>()

    fun init() {
        if (!initialised) {
            initialised = true
            getToken()
            fetchData()
        }
    }

    override fun onCleared() {
        disposable.dispose()
        disposable.clear()
        super.onCleared()
    }

    fun onPlanetClicked(planetName: String) {
        if (isFindEnabled()) {
            //TODO: move string to resources
            _message.postValue("You have selected enough planet, click Find")
            return
        }
        val planet = planets.first { it.name == planetName }
        _navigation.value =
            NavigationEvent.ShowVehicleSelection(planet, ArrayList(getAvailableVehiclesFor(planet)))
    }

    fun onActivityResult(planet: Planet, selectedVehicle: Vehicle?) {
        selectedItems[planet] = selectedVehicle
        _viewState.postValue(ViewState(selectedItems, isFindEnabled()))
    }

    fun onFindClicked() {
        _navigation.value = NavigationEvent.ShowResult(
            ArrayList(selectedItems.filter { it.value != null }.keys),
            ArrayList(selectedItems.values.mapNotNull { it })
        )
    }

    private fun getToken() {
        repository.getToken()
            .doOnError { _message.postValue(resolveError(it)) }
            .subscribe()
            .addTo(disposable)
    }

    private fun getAvailableVehiclesFor(planet: Planet): List<Vehicle> {
        val list = mutableListOf<Vehicle>()
        for (item in vehicles) {
            when (val count = selectedItems.values.count { it == item }) {
                0 -> list.add(item)
                else -> {
                    if (item.amount - count > 0) {
                        list.add(item)
                    }
                }
            }
        }

        return list.filter { it.maxDistance >= planet.distance }
    }

    private fun fetchData() {
        repository.getPlanets()
            .flatMap { planets -> repository.getVehicles().map { Pair(planets, it) } }
            .doOnSubscribe { _loading.postValue(Loading.Show) }
            .doAfterTerminate { _loading.postValue(Loading.Hide) }
            .subscribe(::onSuccess, ::onFailure)
            .addTo(disposable)
    }

    private fun isFindEnabled(): Boolean =
        selectedItems.values.filterNotNull().count() == 4

    private fun onSuccess(data: Pair<List<Planet>, List<Vehicle>>) {
        planets = data.first
        vehicles = data.second
        planets.forEach {
            selectedItems[it] = null
        }
        _viewState.postValue(ViewState(selectedItems, isFindEnabled()))
    }

    private fun onFailure(it: Throwable) {
        _message.postValue(resolveError(it))
        //TODO: good to have a nicer way for failure
    }

    // for now having errors here, but better to have domain exception and map some exception from data layer to domain meaningful exception
    private fun resolveError(error: Throwable) =
        when (error) {
            is SocketException,
            is UnknownHostException -> "No Network Error"
            is SocketTimeoutException -> "Timeout Error"
            else -> "Something went wrong"
        }

    data class ViewState(val planetVehicle: Map<Planet, Vehicle?>, val isFindEnabled: Boolean)

    sealed class NavigationEvent {
        data class ShowVehicleSelection(val selectedPlanet: Planet, val vehicles: ArrayList<Vehicle>) : NavigationEvent()
        data class ShowResult(val planets: ArrayList<Planet>, val vehicles: ArrayList<Vehicle>) : NavigationEvent()
    }

    sealed class Loading {
        object Show : Loading()
        object Hide : Loading()
    }
}