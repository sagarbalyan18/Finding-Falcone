package com.example.findingfalcone.data.model

import com.squareup.moshi.Json

data class FindApiRequest(
    val token: String,
    @Json(name = "planet_names") val planetNames: List<String>,
    @Json(name = "vehicle_names") val VehicleNames: List<String>
)