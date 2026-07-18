package com.example.tripplanner.gmaps.tools.models

data class AttractionSearchRequest(

    val destination: String,

    val interests: List<String> = emptyList(),

    val maxResults: Int = 10
)

data class AttractionSearchResponse(

    val attractions: List<Attraction>
) {

    data class Attraction(

        val id: String,

        val name: String,

        val description: String?,

        val address: String,

        val rating: Double?,

        val mapsUrl: String,

        val latitude: Double,

        val longitude: Double
    )
}