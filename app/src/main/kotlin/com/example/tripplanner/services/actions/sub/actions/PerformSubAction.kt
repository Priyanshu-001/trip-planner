package com.example.tripplanner.services.actions.sub.actions

import com.example.tripplanner.TripSession
import com.example.tripplanner.services.TripSessionDataServiceImpl
import com.example.tripplanner.services.actions.ingest
import com.example.tripplanner.travel.ai.agents.AgentEnum
import com.example.tripplanner.travel.ai.agents.sub.agents.SubAgent
import org.springframework.stereotype.Service

@Service
class SubActionService(
    private val subAgents: List<SubAgent<*>>,
    private val tripSessionDataServiceImpl: TripSessionDataServiceImpl
) : SubAction {
    private val subAgentBySubAgentType = subAgents.associateBy { it.getType() }

    override suspend fun perform(
        tripSession: TripSession,
        subActionType: SubActionType
    ) {
        try {
            val locallyLockedTripSession = tripSessionDataServiceImpl.markSubTaskAsPending(tripSession, subActionType)
            val subAgent =
                subAgentBySubAgentType[subActionType.toAgent()] ?: throw RuntimeException() //TODO: Correct exceptions
            val agentInstructions = tripSession.getAgentInstructionBySubAction(subActionType)
            val subAgentOutput = subAgent.perform(agentInstructions)
            locallyLockedTripSession.ingest(subAgentOutput, subActionType)
            tripSessionDataServiceImpl.saveSubTaskResult(locallyLockedTripSession,subActionType)
        } finally {
            tripSessionDataServiceImpl.markSubTaskAsNotStarted(tripSession, subActionType)
        }
    }
}

private fun SubActionType.toAgent() = when (this) {
    SubActionType.STAY_SUGGESTIONS -> AgentEnum.STAY_AGENT
    SubActionType.TRANSIT_SUGGESTIONS -> AgentEnum.TRANSIT_AGENT
    SubActionType.ATTRACTION_SUGGESTIONS -> AgentEnum.ATTRACTION_AGENT
    SubActionType.FOOD_SUGGESTIONS -> AgentEnum.RESTAURANT_AGENT

}
