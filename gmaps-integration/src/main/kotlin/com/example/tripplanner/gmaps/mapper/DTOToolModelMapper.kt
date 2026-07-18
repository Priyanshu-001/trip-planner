package com.example.tripplanner.gmaps.mapper

import com.example.tripplanner.gmaps.dto.ComputeRoutesResponse
import com.example.tripplanner.gmaps.dto.Place
import com.example.tripplanner.gmaps.dto.SearchTextResponse
import com.example.tripplanner.gmaps.tools.models.AttractionSearchResponse
import com.example.tripplanner.gmaps.tools.models.HotelSearchResponse
import com.example.tripplanner.gmaps.tools.models.RestaurantSearchResponse
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