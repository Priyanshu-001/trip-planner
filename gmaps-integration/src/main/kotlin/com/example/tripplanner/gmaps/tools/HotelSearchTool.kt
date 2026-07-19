package com.example.tripplanner.gmaps.tools

import com.example.tripplanner.gmaps.GoogleMapsClient
import com.example.tripplanner.gmaps.mapper.toGoogleRequest
import com.example.tripplanner.gmaps.mapper.toHotelSearchResponse
import com.example.tripplanner.gmaps.tools.models.HotelSearchRequest
import com.example.tripplanner.gmaps.tools.models.HotelSearchResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component

@Component
class HotelSearchTool(
    private val googleMapsClient: GoogleMapsClient
): SubAgentTools {

    @Tool(
        name = "searchHotels",
        description = "Search hotels for a destination."
    )
    suspend fun searchHotels(request: HotelSearchRequest): HotelSearchResponse =
        googleMapsClient
            .searchText(request.toGoogleRequest())
            .toHotelSearchResponse()
}
