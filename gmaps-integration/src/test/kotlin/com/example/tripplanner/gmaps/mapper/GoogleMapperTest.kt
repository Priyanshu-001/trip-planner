package com.example.tripplanner.gmaps.mapper

import com.example.tripplanner.gmaps.tools.models.AttractionSearchRequest
import com.example.tripplanner.gmaps.tools.models.HotelSearchRequest
import com.example.tripplanner.gmaps.tools.models.RestaurantSearchRequest
import com.example.tripplanner.gmaps.tools.models.RouteSearchRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GoogleMapperTest {

    @Test
    fun `hotel requests map to search text queries`() {
        val request = HotelSearchRequest(destination = "Goa", preferences = listOf("Luxury", "pet friendly"), maxResults = 3)

        val googleRequest = request.toGoogleRequest()

        assertEquals("Luxury pet friendly hotels in Goa", googleRequest.textQuery)
        assertEquals(3, googleRequest.pageSize)
    }

    @Test
    fun `restaurant requests map to search text queries`() {
        val request = RestaurantSearchRequest(location = "Goa", preferences = listOf("Seafood"), maxResults = 4)

        val googleRequest = request.toGoogleRequest()

        assertEquals("Seafood restaurants in Goa", googleRequest.textQuery)
        assertEquals(4, googleRequest.pageSize)
    }

    @Test
    fun `attraction requests map to search text queries`() {
        val request = AttractionSearchRequest(destination = "Goa", interests = listOf("beaches", "nightlife"), maxResults = 6)

        val googleRequest = request.toGoogleRequest()

        assertEquals("beaches nightlife attractions in Goa", googleRequest.textQuery)
        assertEquals(6, googleRequest.pageSize)
    }

    @Test
    fun `route requests map to compute routes requests`() {
        val request = RouteSearchRequest(
            source = RouteSearchRequest.Coordinate(latitude = 1.0, longitude = 2.0),
            destination = RouteSearchRequest.Coordinate(latitude = 3.0, longitude = 4.0),
            travelMode = RouteSearchRequest.TravelMode.WALK
        )

        val googleRequest = request.toGoogleRequest()

        assertEquals(1.0, googleRequest.origin.location.latLng.latitude)
        assertEquals(2.0, googleRequest.origin.location.latLng.longitude)
        assertEquals(3.0, googleRequest.destination.location.latLng.latitude)
        assertEquals(4.0, googleRequest.destination.location.latLng.longitude)
        assertEquals(com.example.tripplanner.gmaps.dto.ComputeRoutesRequest.TravelMode.WALK, googleRequest.travelMode)
        assertTrue(googleRequest.computeAlternativeRoutes.not())
    }
}
