package com.example.tripplanner.gmaps.tools.models

data class RestaurantSearchRequest(

    val location: String,

    val preferences: List<String> = emptyList(),

    val maxResults: Int = 10
)

data class RestaurantSearchResponse(

    val restaurants: List<Restaurant>
) {

    data class Restaurant(

        val id: String,

        val name: String,

        val address: String,

        val rating: Double?,

        val mapsUrl: String,

        val latitude: Double,

        val longitude: Double
    )
}