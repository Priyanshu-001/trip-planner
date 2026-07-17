package com.example.tripplanner
import models.Requirements
import java.util.UUID

class TripSession(
    val id: String = UUID.randomUUID().toString(),
    val initialPrompt: String,
    var status: Status = Status.INITIALIZED,
    val requirements: Requirements = Requirements(),
    val followUpQuestions: MutableList<FollowUpQuestion> = mutableListOf(),

    ) {


    enum class Status {
        INITIALIZED, COLLECTING_REQUIREMENTS, WAITING_FOR_USER_INPUT, READY_FOR_PLANNING, PLANNING, COMPLETED, FAILED
    }

    data class Requirements(

        var destinationCity: String? = null,

        var sourceCity: String? = null,

        var tripDuration: Duration? = null,

        var numberOfTravelers: Int? = null,

        var includePets: Boolean = false,

        var specialRequests: MutableList<String> = mutableListOf(),

        var interests: MutableList<String> = mutableListOf()

    ) {

        data class Duration(
            var value: Int = 0, var unit: Unit = Unit.DAYS
        ) {
            enum class Unit {
                DAYS, HOURS
            }
        }
        fun toDTO(): models.Requirements {
            return Requirements(
                destinationCity = this.destinationCity,
                sourceCity = this.sourceCity,
                tripDuration = models.Requirements.Duration(
                    this.tripDuration?.value ?: 0, this.tripDuration?.unit?.toDTO() ?: models.Requirements.Duration.Unit.HOURS
                ),
                numberOfTravelers = this.numberOfTravelers,
                includePets = this.includePets,
                specialRequests = this.specialRequests,
                interests = this.interests
            )
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

open class TripSessionFactory {
    companion object {
        public fun createTripSession(prompt: String): TripSession {
            return TripSession(initialPrompt = prompt)
        }
    }
}


private fun TripSession.Requirements.Duration.Unit.toDTO(): models.Requirements.Duration.Unit {
    return when (this) {
        TripSession.Requirements.Duration.Unit.DAYS -> models.Requirements.Duration.Unit.DAYS
        TripSession.Requirements.Duration.Unit.HOURS -> models.Requirements.Duration.Unit.HOURS
    }
}