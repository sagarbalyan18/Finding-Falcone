package com.example.findingfalcone.data

import com.example.findingfalcone.data.model.FindApiResponse
import com.example.findingfalcone.data.model.PlanetsApiResponse
import com.example.findingfalcone.data.model.VehiclesApiResponse
import com.example.findingfalcone.domain.model.FindResponse
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.domain.model.Vehicle
import org.junit.Test
import kotlin.test.assertEquals

class ServiceMapperKtTest {

    private val mockPlanetsApiResponse = PlanetsApiResponse("planetx", 1234)
    private val mockVehiclesApiResponse = VehiclesApiResponse("shuttle", 1, 500, 4)
    private val mockFindApiResponseSuccess = FindApiResponse("planetz", "success", null)
    private val mockFindApiResponseFailure = FindApiResponse(null, "false", null)

    @Test
    fun mapToPlanet() {
        val expectedPlanet = Planet("planetx", 1234)
        assertEquals(expectedPlanet, mockPlanetsApiResponse.mapToPlanet())
    }

    @Test
    fun mapToVehicle() {
        val expectedVehicle = Vehicle("shuttle", 1, 500, 4)
        assertEquals(expectedVehicle, mockVehiclesApiResponse.mapToVehicle())
    }

    @Test
    fun mapToFindResponseSuccess() {
        val expected = FindResponse.Success("planetz")
        assertEquals(expected, mockFindApiResponseSuccess.mapToFindResponse())
    }

    @Test
    fun mapToFindResponseFailure() {
        val expected = FindResponse.Failure
        assertEquals(expected, mockFindApiResponseFailure.mapToFindResponse())
    }
}