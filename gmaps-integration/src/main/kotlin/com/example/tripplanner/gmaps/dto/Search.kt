package com.example.tripplanner.gmaps.dto


data class SearchTextRequest(

    val textQuery: String,

    val languageCode: String? = null,

    val regionCode: String? = null,

    val pageSize: Int? = null,

    val pageToken: String? = null,

    val includedType: String? = null,

    val openNow: Boolean? = null,

    val minRating: Double? = null,

    val rankPreference: RankPreference? = null,

    val locationBias: LocationBias? = null,

    val locationRestriction: LocationRestriction? = null
) {

    enum class RankPreference {
        RELEVANCE,
        DISTANCE
    }

    data class LocationBias(
        val circle: Circle
    )

    data class LocationRestriction(
        val circle: Circle
    )

    data class Circle(
        val center: LatLng,
        val radius: Double
    )

    data class LatLng(
        val latitude: Double,
        val longitude: Double
    )
}

data class SearchTextResponse(

    val places: List<Place> = emptyList(),

    val nextPageToken: String? = null
)

data class Place(

    val id: String?,

    val displayName: DisplayName?,

    val formattedAddress: String?,

    val shortFormattedAddress: String?,

    val googleMapsUri: String?,

    val websiteUri: String?,

    val nationalPhoneNumber: String?,

    val internationalPhoneNumber: String?,

    val rating: Double?,

    val userRatingCount: Int?,

    val priceLevel: PriceLevel?,

    val primaryType: String?,

    val primaryTypeDisplayName: DisplayName?,

    val location: LatLng?,

    val viewport: Viewport?,

    val photos: List<Photo> = emptyList(),

    val regularOpeningHours: OpeningHours?,

    val businessStatus: BusinessStatus?
) {

    data class DisplayName(
        val text: String?,
        val languageCode: String?
    )

    data class LatLng(
        val latitude: Double?,
        val longitude: Double?
    )

    data class Viewport(
        val low: LatLng?,
        val high: LatLng?
    )

    data class Photo(
        val name: String?,
        val widthPx: Int?,
        val heightPx: Int?
    )

    data class OpeningHours(
        val openNow: Boolean?
    )

    enum class PriceLevel {
        PRICE_LEVEL_FREE,
        PRICE_LEVEL_INEXPENSIVE,
        PRICE_LEVEL_MODERATE,
        PRICE_LEVEL_EXPENSIVE,
        PRICE_LEVEL_VERY_EXPENSIVE
    }

    enum class BusinessStatus {
        OPERATIONAL,
        CLOSED_TEMPORARILY,
        CLOSED_PERMANENTLY
    }
}