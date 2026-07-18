package com.example.tripplanner.gmaps.tools

import com.example.tripplanner.gmaps.GoogleMapsClient
import org.springframework.ai.tool.annotation.Tool

class HotelSearchToo (
    private val googleMapsClient: GoogleMapsClient
) {
    @Tool(description = "Add two integers and return the sum")
}