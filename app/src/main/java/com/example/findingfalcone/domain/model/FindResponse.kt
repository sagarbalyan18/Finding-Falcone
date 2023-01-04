package com.example.findingfalcone.domain.model

sealed class FindResponse() {
    data class Success(val planetName: String) : FindResponse()
    object Failure : FindResponse()
}