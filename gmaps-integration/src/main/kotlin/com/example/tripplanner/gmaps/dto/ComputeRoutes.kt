package com.example.tripplanner.gmaps.dto

data class ComputeRoutesRequest(

    val origin: Waypoint,

    val destination: Waypoint,

    val travelMode: TravelMode = TravelMode.DRIVE,

    val routingPreference: RoutingPreference? = null,

    val computeAlternativeRoutes: Boolean = false,

    val languageCode: String? = null,

    val units: Units? = null
) {

    data class Waypoint(
        val location: Location
    )

    data class Location(
        val latLng: LatLng
    )

    data class LatLng(
        val latitude: Double,
        val longitude: Double
    )

    enum class TravelMode {
        DRIVE,
        WALK,
        BICYCLE,
        TRANSIT
    }

    enum class RoutingPreference {
        TRAFFIC_UNAWARE,
        TRAFFIC_AWARE,
        TRAFFIC_AWARE_OPTIMAL
    }

    enum class Units {
        METRIC,
        IMPERIAL
    }
}

data class ComputeRoutesResponse(

    val routes: List<Route> = emptyList()
) {

    data class Route(

        val distanceMeters: Int?,

        val duration: String?,

        val staticDuration: String?,

        val polyline: Polyline?,

        val localizedValues: LocalizedValues?,

        val legs: List<Leg> = emptyList()
    )

    data class Polyline(
        val encodedPolyline: String?
    )

    data class LocalizedValues(

        val distance: LocalizedText?,

        val duration: LocalizedText?
    )

    data class LocalizedText(
        val text: String?
    )

    data class Leg(

        val distanceMeters: Int?,

        val duration: String?,

        val staticDuration: String?,

        val startLocation: Location?,

        val endLocation: Location?
    )

    data class Location(
        val latLng: LatLng?
    )

    data class LatLng(
        val latitude: Double?,
        val longitude: Double?
    )
}