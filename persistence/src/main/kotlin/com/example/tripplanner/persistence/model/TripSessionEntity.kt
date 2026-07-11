package com.example.tripplanner.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document("trip_sessions")
data class TripSessionEntity(

    @Id
    val id: String = UUID.randomUUID().toString(),

    var initialPrompt: String,

    var status: Status = Status.INITIALIZED,

    var requirements: Requirements = Requirements(),

    var followUpQuestions: MutableList<FollowUpQuestion> = mutableListOf(),

    val createdAt: Instant = Instant.now(),

    var updatedAt: Instant = Instant.now()

) {


    enum class Status {
        INITIALIZED, COLLECTING_REQUIREMENTS, WAITING_FOR_USER_INPUT, READY_FOR_PLANNING, PLANNING, COMPLETED, FAILED
    }
    data class Requirements(

        var destination: String? = null,

        var source: String? = null,

        var tripDuration: Duration? = null,

        var numberOfTravelers: Int? = null,

        var includePets: Boolean = false,

        var specialRequests: MutableList<String> = mutableListOf(),

        var interests: MutableList<String> = mutableListOf()

    ) {

        data class Duration(
            var value: Int = 0,
            var unit: Unit = Unit.DAYS
        ) {
            enum class Unit {
                DAYS,
                HOURS
            }
        }
    }

    data class FollowUpQuestion(

        val id: String = UUID.randomUUID().toString(),

        var question: String,

        var answerChoices: List<String>? = null,

        var answer: String? = null,

        var answerProcessed: Boolean = false
    )
}