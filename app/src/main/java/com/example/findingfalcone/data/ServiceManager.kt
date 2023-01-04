package com.example.findingfalcone.data

import com.example.findingfalcone.application.LocalProperties
import com.example.findingfalcone.data.model.FindApiRequest
import com.example.findingfalcone.domain.Repository
import com.example.findingfalcone.domain.model.FindResponse
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.domain.model.Vehicle
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceManager @Inject constructor(
    private val service: Service,
    private val localProperties: LocalProperties
) : Repository {
    override fun getToken(): Completable = service.getToken()
        .map {
            localProperties.token = it.token // update token in the shared preferences
        }
        .ignoreElement()
        .onErrorResumeNext { Completable.error(it) }
        .subscribeOn(Schedulers.io())

    override fun getPlanets(): Single<List<Planet>> = service.getPlanets()
        .map { it.map { item -> item.mapToPlanet() } }
        .onErrorResumeNext { Single.error(it) }
        .subscribeOn(Schedulers.io())

    override fun getVehicles(): Single<List<Vehicle>> = service.getVehicles()
        .map { it.map { item -> item.mapToVehicle() } }
        .onErrorResumeNext { Single.error(it) }
        .subscribeOn(Schedulers.io())

    override fun findPrinces(planets: List<Planet>, vehicles: List<Vehicle>): Single<FindResponse> =
        localProperties.token?.let { token ->
            service.findPrinces(FindApiRequest(token, planets.map { it.name }, vehicles.map { it.name }))
                .map { it.mapToFindResponse() }
                .onErrorResumeNext { Single.error(it) }
                .subscribeOn(Schedulers.io())
        } ?: Single.error(NoSuchElementException("Token is missing"))
}