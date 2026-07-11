package com.example.tripplanner

import com.example.tripplanner.services.PlanFlowOrchestrator
import com.example.tripplanner.services.api.Response
import com.example.tripplanner.services.api.TripResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class HelloController(val planFlowOrchestrator: PlanFlowOrchestrator) {

    @GetMapping("/hello")
    suspend fun hello(): String {
        return "Hello from Trip Planner AI 🚀"
    }

    @PostMapping("/trip")
    suspend fun planTrip(@RequestBody tripPlanningRequest: TripPlanningRequest): Response {
        return planFlowOrchestrator.handleNewTripPlanningRequest(tripPlanningRequest).toResponse()
    }
    @PutMapping("/trip/{tripId}")
    suspend fun planTrip(@RequestParam tripId: String, @RequestBody answers: TripPlanningAnswer)  {

    }
}

private fun TripSession.toResponse(): Response {
    return when (status) {
        TripSession.Status.WAITING_FOR_USER_INPUT ->
            Response.QuestionAnswering(
                questions = followUpQuestions.map {
                    com.example.tripplanner.services.api.FollowUpQuestion(
                        id = it.id,
                        question = it.question,
                        answerChoices = it.answerChoices
                    )
                }
            )

        else ->
            Response.Done(
                tripResult = TripResult(
                    destination = requirements.destination.orEmpty(),
                    summary = ""
                )
            )
    }
}
