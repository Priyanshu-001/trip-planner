package com.example.tripplanner.persistence.reposistory

import com.example.tripplanner.persistence.model.TripSessionEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Component

@Component
interface SpringDataTripSessionRepository :
    ReactiveMongoRepository<TripSessionEntity, String>