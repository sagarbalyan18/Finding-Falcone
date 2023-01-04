package com.example.findingfalcone.data

import com.example.findingfalcone.data.model.FindApiResponse
import com.example.findingfalcone.data.model.PlanetsApiResponse
import com.example.findingfalcone.data.model.VehiclesApiResponse
import com.example.findingfalcone.domain.model.FindResponse
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.domain.model.Vehicle

fun PlanetsApiResponse.mapToPlanet() = Planet(name, distance)

fun VehiclesApiResponse.mapToVehicle() = Vehicle(name, amount, maxDistance, speed)

fun FindApiResponse.mapToFindResponse() =
    when (status) {
        "success" -> FindResponse.Success(planetName!!)
        "false" -> FindResponse.Failure
        else -> throw SecurityException(error)
    }