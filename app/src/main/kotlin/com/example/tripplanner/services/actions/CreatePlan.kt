package com.example.tripplanner.services.actions

import com.example.tripplanner.TripSession
import com.example.tripplanner.services.ActionHandler
import com.example.tripplanner.services.ActionRegistry
import com.example.tripplanner.travel.ai.agents.OrchestrateTripRequest
import com.example.tripplanner.travel.ai.agents.OrchestratorAgent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreatePlan(val orchestratorAgent: OrchestratorAgent): ActionHandler {
    override suspend fun perform(tripSession: TripSession): TripSession {
       val request =  OrchestrateTripRequest( tripSession.requirements.toDTO(), tripSession.initialPrompt)
       val res =  orchestratorAgent.orchestrateTrip(request)
        logger.info(res.toString())
        return  tripSession
    }

    companion object{
        private val logger = LoggerFactory.getLogger(CreatePlan::class.java)
    }

    override fun getAction(): ActionRegistry.Action {
        return ActionRegistry.Action.PLAN_TRIP
    }
}