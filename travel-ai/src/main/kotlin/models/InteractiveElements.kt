package models

sealed interface InteractiveElement {

    val id: String
    val title: String

    data class Hotel(
        override val id: String,
        override val title: String,
        val providerId: String,
        val rating: Double,
        val address: String,
        val mapsUrl: String,
        val imageUrl: String?
    ) : InteractiveElement

    data class Attraction(
        override val id: String,
        override val title: String,
        val description: String,
        val mapUrl: String
    ) : InteractiveElement

    data class Restaurant(
        override val id: String,
        override val title: String,
        val description: String,
        val mapUrl: String
    ) : InteractiveElement

    data class Route(
        override val id: String,
        override val title: String,
        val duration: String?,
        val distance: String?,
        val mode: Mode,
        val mapsUrl: String?
    ) : InteractiveElement {

        enum class Mode {
            WALK,
            CAR,
            TRAIN,
            PLANE
        }
    }

    data class FlightSuggestion(
        override val id: String,
        override val title: String,
        val sourceAirport: String,
        val destinationAirport: String,
        val reason: String
    ) : InteractiveElement
}