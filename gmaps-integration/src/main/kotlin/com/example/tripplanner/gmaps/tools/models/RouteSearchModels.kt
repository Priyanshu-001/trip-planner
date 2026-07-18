package com.example.tripplanner.gmaps.tools.models

data class RouteSearchRequest(

    val source: Coordinate,

    val destination: Coordinate,

    val travelMode: TravelMode = TravelMode.DRIVE
) {

    data class Coordinate(

        val latitude: Double,

        val longitude: Double
    )

    enum class TravelMode {
        DRIVE,
        WALK,
        TRANSIT
    }
}

data class RouteSearchResponse(

    val routes: List<Route>
) {

    data class Route(

        val distanceMeters: Int,

        val duration: String,

        val encodedPolyline: String?,

        val navigationUrl: String?
    )
}