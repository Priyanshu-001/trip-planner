package com.example.tripplanner.services

import com.example.tripplanner.TripPlanningAnswer
import com.example.tripplanner.TripPlanningRequest
import com.example.tripplanner.TripSession
import com.example.tripplanner.TripSessionFactory
import org.springframework.stereotype.Service

@Service
class PlanFlowOrchestrator(val tripDataService: TripSessionDataServiceImpl, val actionRegistry: ActionRegistry) {

    suspend fun handleNewTripPlanningRequest(tripPlanningRequest: TripPlanningRequest): TripSession {
        val tripSession = TripSessionFactory.createTripSession(tripPlanningRequest.initialPrompt)
        return actionRegistry.getActionHandler(ActionRegistry.Action.GENERATE_REQUIREMENTS).perform(tripSession)
    }

    suspend fun handleAnswers(tripId: String, answers: TripPlanningAnswer): TripSession {
       val tripSession =  tripDataService.findById(tripId)
        //TODO: add conditions if its already orchestrated
       tripSession!!.enrichWithAnswers(answers)
        val nTripSession = actionRegistry.getActionHandler(ActionRegistry.Action.GENERATE_REQUIREMENTS).perform(tripSession)
//        launchOrhestratorifNeeded(nTripSession)
        actionRegistry.getActionHandler(ActionRegistry.Action.PLAN_TRIP).perform(nTripSession)
        return nTripSession
    }

    private fun launchOrhestratorifNeeded(nTripSession: TripSession) {

    }

}

private fun TripSession.enrichWithAnswers(answers: TripPlanningAnswer) {

    val unAnsweredQuestions = this.followUpQuestions.filter { !it.answerProcessed }
    val answerByIdMap = answers.answers?.associate { it.id to it.answer } ?: mapOf()
    unAnsweredQuestions.forEach { it.answer = answerByIdMap[it.id] }
}
