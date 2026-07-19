package com.example.tripplanner.gmaps.mapper

import com.example.tripplanner.gmaps.dto.ComputeRoutesRequest
import com.example.tripplanner.gmaps.dto.ComputeRoutesResponse
import com.example.tripplanner.gmaps.dto.Place
import com.example.tripplanner.gmaps.dto.SearchTextRequest
import com.example.tripplanner.gmaps.dto.SearchTextResponse
import com.example.tripplanner.gmaps.tools.models.AttractionSearchRequest
import com.example.tripplanner.gmaps.tools.models.AttractionSearchResponse
import com.example.tripplanner.gmaps.tools.models.HotelSearchRequest
import com.example.tripplanner.gmaps.tools.models.HotelSearchResponse
import com.example.tripplanner.gmaps.tools.models.RestaurantSearchRequest
import com.example.tripplanner.gmaps.tools.models.RestaurantSearchResponse
import com.example.tripplanner.gmaps.tools.models.RouteSearchRequest
import com.example.tripplanner.gmaps.tools.models.RouteSearchResponse


fun SearchTextResponse.toHotelSearchResponse(): HotelSearchResponse =
    HotelSearchResponse(
        hotels = places.mapNotNull { it.toHotel() }
    )

private fun Place.toHotel(): HotelSearchResponse.Hotel? {

    val location = location ?: return null

    return HotelSearchResponse.Hotel(
        id = id ?: return null,
        name = displayName?.text ?: return null,
        address = formattedAddress.orEmpty(),
        rating = rating,
        mapsUrl = googleMapsUri.orEmpty(),
        latitude = location.latitude ?: 0.0,
        longitude = location.longitude ?: 0.0,
        imageUrl = photos.firstOrNull()?.name
    )
}

fun SearchTextResponse.toRestaurantSearchResponse(): RestaurantSearchResponse =
    RestaurantSearchResponse(
        restaurants = places.mapNotNull { it.toRestaurant() }
    )

private fun Place.toRestaurant(): RestaurantSearchResponse.Restaurant? {

    val location = location ?: return null

    return RestaurantSearchResponse.Restaurant(
        id = id ?: return null,
        name = displayName?.text ?: return null,
        address = formattedAddress.orEmpty(),
        rating = rating,
        mapsUrl = googleMapsUri.orEmpty(),
        latitude = location.latitude ?: 0.0,
        longitude = location.longitude ?: 0.0
    )
}

fun SearchTextResponse.toAttractionSearchResponse(): AttractionSearchResponse =
    AttractionSearchResponse(
        attractions = places.mapNotNull { it.toAttraction() }
    )

private fun Place.toAttraction(): AttractionSearchResponse.Attraction? {

    val location = location ?: return null

    return AttractionSearchResponse.Attraction(
        id = id ?: return null,
        name = displayName?.text ?: return null,
        description = primaryTypeDisplayName?.text,
        address = formattedAddress.orEmpty(),
        rating = rating,
        mapsUrl = googleMapsUri.orEmpty(),
        latitude = location.latitude ?: 0.0,
        longitude = location.longitude ?: 0.0
    )
}

fun ComputeRoutesResponse.toRouteSearchResponse(): RouteSearchResponse =
    RouteSearchResponse(
        routes = routes.map { it.toRoute() }
    )

private fun ComputeRoutesResponse.Route.toRoute() =
    RouteSearchResponse.Route(
        distanceMeters = distanceMeters ?: 0,
        duration = duration.orEmpty(),
        encodedPolyline = polyline?.encodedPolyline,
        navigationUrl = null
    )

fun HotelSearchRequest.toGoogleRequest(): SearchTextRequest {
    val destination = destination.trim()
    val preferencesText = preferences.filter { it.isNotBlank() }.joinToString(" ")
    val textQuery = if (preferencesText.isBlank()) {
        "Hotels in $destination"
    } else {
        "$preferencesText hotels in $destination"
    }

    return SearchTextRequest(
        textQuery = textQuery,
        pageSize = maxResults
    )
}

fun RestaurantSearchRequest.toGoogleRequest(): SearchTextRequest {
    val location = location.trim()
    val preferencesText = preferences.filter { it.isNotBlank() }.joinToString(" ")
    val textQuery = if (preferencesText.isBlank()) {
        "restaurants in $location"
    } else {
        "$preferencesText restaurants in $location"
    }

    return SearchTextRequest(
        textQuery = textQuery,
        pageSize = maxResults
    )
}

fun AttractionSearchRequest.toGoogleRequest(): SearchTextRequest {
    val destination = destination.trim()
    val interestsText = interests.filter { it.isNotBlank() }.joinToString(" ")
    val textQuery = if (interestsText.isBlank()) {
        "attractions in $destination"
    } else {
        "$interestsText attractions in $destination"
    }

    return SearchTextRequest(
        textQuery = textQuery,
        pageSize = maxResults
    )
}

fun RouteSearchRequest.toGoogleRequest(): ComputeRoutesRequest =
    ComputeRoutesRequest(
        origin = ComputeRoutesRequest.Waypoint(
            location = ComputeRoutesRequest.Location(
                latLng = ComputeRoutesRequest.LatLng(
                    latitude = source.latitude,
                    longitude = source.longitude
                )
            )
        ),
        destination = ComputeRoutesRequest.Waypoint(
            location = ComputeRoutesRequest.Location(
                latLng = ComputeRoutesRequest.LatLng(
                    latitude = destination.latitude,
                    longitude = destination.longitude
                )
            )
        ),
        travelMode = when (travelMode) {
            RouteSearchRequest.TravelMode.DRIVE -> ComputeRoutesRequest.TravelMode.DRIVE
            RouteSearchRequest.TravelMode.WALK -> ComputeRoutesRequest.TravelMode.WALK
            RouteSearchRequest.TravelMode.TRANSIT -> ComputeRoutesRequest.TravelMode.TRANSIT
        }
    )