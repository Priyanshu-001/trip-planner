package com.example.tripplanner.gmaps.tools

import com.example.tripplanner.gmaps.GoogleMapsClient
import com.example.tripplanner.gmaps.mapper.toAttractionSearchResponse
import com.example.tripplanner.gmaps.mapper.toGoogleRequest
import com.example.tripplanner.gmaps.tools.models.AttractionSearchRequest
import com.example.tripplanner.gmaps.tools.models.AttractionSearchResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component

@Component
class AttractionSearchTool(
    private val googleMapsClient: GoogleMapsClient
): SubAgentTools {

    @Tool(
        name = "searchAttractions",
        description = "Search tourist attractions matching user interests."
    )
    suspend fun searchAttractions(request: AttractionSearchRequest): AttractionSearchResponse =
        googleMapsClient
            .searchText(request.toGoogleRequest())
            .toAttractionSearchResponse()
}
