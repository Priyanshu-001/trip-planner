package com.example.tripplanner.gmaps.tools

import com.example.tripplanner.gmaps.GoogleMapsClient
import com.example.tripplanner.gmaps.mapper.toGoogleRequest
import com.example.tripplanner.gmaps.mapper.toRouteSearchResponse
import com.example.tripplanner.gmaps.tools.models.RouteSearchRequest
import com.example.tripplanner.gmaps.tools.models.RouteSearchResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component

@Component
class RouteSearchTool(
    private val googleMapsClient: GoogleMapsClient
): SubAgentTools {

    @Tool(
        name = "computeRoute",
        description = "Compute a travel route between two locations."
    )
    suspend fun computeRoute(request: RouteSearchRequest): RouteSearchResponse =
        googleMapsClient
            .computeRoutes(request.toGoogleRequest())
            .toRouteSearchResponse()
}
