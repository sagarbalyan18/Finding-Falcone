package com.example.findingfalcone.domain

import com.example.findingfalcone.domain.model.FindResponse
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.domain.model.Vehicle
import io.reactivex.Completable
import io.reactivex.Single

interface Repository {
    fun getToken(): Completable

    fun getPlanets(): Single<List<Planet>>
    fun getVehicles(): Single<List<Vehicle>>

    fun findPrinces(planets: List<Planet>, vehicles: List<Vehicle>): Single<FindResponse>
}