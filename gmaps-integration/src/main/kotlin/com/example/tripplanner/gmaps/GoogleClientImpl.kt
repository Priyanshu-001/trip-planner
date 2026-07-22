package com.example.tripplanner.gmaps

import com.example.tripplanner.gmaps.dto.ComputeRoutesRequest
import com.example.tripplanner.gmaps.dto.ComputeRoutesResponse
import com.example.tripplanner.gmaps.dto.SearchTextRequest
import com.example.tripplanner.gmaps.dto.SearchTextResponse
import kotlinx.coroutines.reactive.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GoogleMapsClientImpl(

    @Value("\${google.maps.api-key}")
    private val apiKey: String,

    builder: WebClient.Builder

) : GoogleMapsClient {

    private val placesClient = builder
        .baseUrl("https://places.googleapis.com")
        .defaultHeader("X-Goog-Api-Key", apiKey)
        .build()

    private val routesClient = builder
        .baseUrl("https://routes.googleapis.com")
        .defaultHeader("X-Goog-Api-Key", apiKey)
        .build()

    override suspend fun searchText(
        request: SearchTextRequest
    ): SearchTextResponse {
        log.info("Lets gooo")
        return placesClient
            .post()
            .uri("/v1/places:searchText")
            .header(
                "X-Goog-FieldMask",
                "places.id," +
                        "places.displayName," +
                        "places.formattedAddress," +
                        "places.location," +
                        "places.rating," +
                        "places.userRatingCount," +
                        "places.photos," +
                        "places.googleMapsUri," +
                        "nextPageToken"
            )
            .bodyValue(request)
            .retrieve()
            .bodyToMono(SearchTextResponse::class.java)
            .awaitSingle()
    }

    override suspend fun computeRoutes(
        request: ComputeRoutesRequest
    ): ComputeRoutesResponse {
        log.info("Lets goo again")
        return routesClient
            .post()
            .uri("/directions/v2:computeRoutes")
            .header(
                "X-Goog-FieldMask",
                "routes.distanceMeters," +
                        "routes.duration," +
                        "routes.staticDuration," +
                        "routes.polyline.encodedPolyline," +
                        "routes.localizedValues," +
                        "routes.legs"
            )
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ComputeRoutesResponse::class.java)
            .awaitSingle()
    }

    companion object {
        private val log = LoggerFactory.getLogger(GoogleMapsClientImpl::class.java)
    }
}