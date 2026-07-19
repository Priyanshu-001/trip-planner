package com.example.tripplanner.gmaps.tools

import com.example.tripplanner.gmaps.GoogleMapsClient
import com.example.tripplanner.gmaps.mapper.toGoogleRequest
import com.example.tripplanner.gmaps.mapper.toRestaurantSearchResponse
import com.example.tripplanner.gmaps.tools.models.RestaurantSearchRequest
import com.example.tripplanner.gmaps.tools.models.RestaurantSearchResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component

@Component
class RestaurantSearchTool(
    private val googleMapsClient: GoogleMapsClient
): SubAgentTools {

    @Tool(
        name = "searchRestaurants",
        description = "Search restaurants near a destination."
    )
    suspend fun searchRestaurants(request: RestaurantSearchRequest): RestaurantSearchResponse =
        googleMapsClient
            .searchText(request.toGoogleRequest())
            .toRestaurantSearchResponse()
}
