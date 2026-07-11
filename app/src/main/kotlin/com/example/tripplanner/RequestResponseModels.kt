package com.example.tripplanner

data class AnsweredQuestion(val id: String, val answer: String)
data class TripPlanningAnswer(
    val answers: List<AnsweredQuestion> ? = emptyList()
)

data class TripPlanningRequest(val initialPrompt: String)