package com.example.tripplanner.gmaps.tools.models

data class HotelSearchRequest(

    val destination: String,

    val preferences: List<String> = emptyList(),

    val maxResults: Int = 5
)

data class HotelSearchResponse(

    val hotels: List<Hotel>
) {

    data class Hotel(

        val id: String,

        val name: String,

        val address: String,

        val rating: Double?,

        val mapsUrl: String,

        val latitude: Double,

        val longitude: Double,

        val imageUrl: String?
    )
}