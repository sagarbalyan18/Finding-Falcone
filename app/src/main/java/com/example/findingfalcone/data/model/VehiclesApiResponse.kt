package com.example.findingfalcone.data.model

import com.squareup.moshi.Json

data class VehiclesApiResponse(
    val name: String,
    @Json(name = "total_no") val amount: Int,
    @Json(name = "max_distance") val maxDistance: Int,
    val speed: Int
)