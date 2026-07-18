package com.example.tripplanner.gmaps

import com.example.tripplanner.gmaps.dto.ComputeRoutesRequest
import com.example.tripplanner.gmaps.dto.ComputeRoutesResponse
import com.example.tripplanner.gmaps.dto.SearchTextRequest
import com.example.tripplanner.gmaps.dto.SearchTextResponse

interface GoogleMapsClient {

    suspend fun searchText(
        request: SearchTextRequest
    ): SearchTextResponse

    suspend fun computeRoutes(
        request: ComputeRoutesRequest
    ): ComputeRoutesResponse
}