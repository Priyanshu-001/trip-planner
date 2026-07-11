package com.example.tripplanner.services.api

sealed interface Response {

    val stage: Stage

    enum class Stage {
        QUESTION_ANSWERING,
        DONE
    }

    data class QuestionAnswering(
        val questions: List<FollowUpQuestion>

    ) : Response {
        override val stage = Stage.QUESTION_ANSWERING
    }

    data class Done(

        val tripResult: TripResult

    ) : Response {
        override val stage = Stage.DONE
    }
}

data class FollowUpQuestion(

    val id: String,

    val question: String,

    val answerChoices: List<String>? = null
)

data class TripResult(

    val destination: String,

    val summary: String,

    val itinerary: List<DayPlan> = emptyList(),

    val hotels: List<Hotel> = emptyList(),

    val experiences: List<Experience> = emptyList(),

    val transportation: List<Transportation> = emptyList()
)

data class DayPlan(

    val day: Int,

    val title: String,

    val activities: List<String> = emptyList()
)

data class Hotel(

    val name: String,

    val address: String,

    val rating: Double? = null
)

data class Experience(

    val name: String,

    val description: String
)

data class Transportation(

    val from: String,

    val to: String,

    val mode: String,

    val notes: String? = null
)