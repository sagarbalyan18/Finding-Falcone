package com.example.findingfalcone.data.model

import com.squareup.moshi.Json

data class FindApiResponse(
    @Json(name = "planet_name") val planetName: String?,
    val status: String?,
    val error: String?
)