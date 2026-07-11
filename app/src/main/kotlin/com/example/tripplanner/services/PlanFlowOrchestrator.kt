package com.example.tripplanner.services

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

}