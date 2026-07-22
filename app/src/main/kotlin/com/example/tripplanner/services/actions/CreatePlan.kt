package com.example.tripplanner.services.actions

import com.example.tripplanner.TripSession
import com.example.tripplanner.services.ActionHandler
import com.example.tripplanner.services.ActionRegistry
import com.example.tripplanner.services.TripSessionDataServiceImpl
import com.example.tripplanner.services.actions.sub.actions.SubActionType
import com.example.tripplanner.travel.ai.agents.AgentEnum
import com.example.tripplanner.travel.ai.agents.OrchestrateTripRequest
import com.example.tripplanner.travel.ai.agents.OrchestrationResult
import com.example.tripplanner.travel.ai.agents.OrchestratorAgent
import com.example.tripplanner.travel.ai.agents.sub.agents.InteractiveElement
import com.example.tripplanner.travel.ai.agents.sub.agents.SubAgentOutput
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.EnumMap

@Service
class CreatePlan(val orchestratorAgent: OrchestratorAgent, val tripSessionDataServiceImpl: TripSessionDataServiceImpl) : ActionHandler {
    override suspend fun perform(tripSession: TripSession): TripSession {
        val request = OrchestrateTripRequest(tripSession.requirements.toDTO(), tripSession.initialPrompt)
        val res = orchestratorAgent.orchestrateTrip(request)
        tripSession.status = TripSession.Status.PLANNING
        tripSession.ingest(res)
        tripSessionDataServiceImpl.save(tripSession)
        return tripSession
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CreatePlan::class.java)
    }

    override fun getAction(): ActionRegistry.Action {
        return ActionRegistry.Action.PLAN_TRIP
    }
}

internal fun TripSession.ingest(res: OrchestrationResult?) {
    val subPlans = EnumMap<SubActionType, TripSession.SubPlan>(SubActionType::class.java)

    res?.nextAgentsInstructions?.forEach { instruction ->
        val subActionType = instruction.agent.toSubActionType() ?: return@forEach
        subPlans[subActionType] = TripSession.SubPlan(
            planResult = TripSession.PlanResult(
                subPlanStatus = TripSession.PlanStatus.READY,
                title = instruction.goal,
                description = instruction.goal,
                status = TripSession.SubPlanStatus.READY,
                interactiveElements = emptyList()
            ),
            subAgentInstructions = models.SubAgentInstructions(
                goal = instruction.goal,
                context = instruction.context,
                requirements = requirements.toDTO()
            )
        )
    }

    plan = TripSession.Plan(
        planStatus = if (subPlans.isEmpty()) TripSession.PlanStatus.NOT_READY else TripSession.PlanStatus.READY,
        subActionToSubPlan = if (subPlans.isEmpty()) null else subPlans
    )
}

internal fun TripSession.ingest(subAgentOutput: SubAgentOutput, subActionType: SubActionType) {
    val existingSubPlan = plan?.subActionToSubPlan?.get(subActionType) ?: return
    val planResult = existingSubPlan.planResult?.copy(
        title = subAgentOutput.oneLineExplanation,
        description = subAgentOutput.detailedExplanation,
        status = TripSession.SubPlanStatus.DONE,
        interactiveElements = subAgentOutput.toInteractiveElements()
    ) ?: TripSession.PlanResult(
        subPlanStatus = TripSession.PlanStatus.READY,
        title = subAgentOutput.oneLineExplanation,
        description = subAgentOutput.detailedExplanation,
        status = TripSession.SubPlanStatus.DONE,
        interactiveElements = subAgentOutput.toInteractiveElements()
    )

    val updatedSubPlan = existingSubPlan.copy(planResult = planResult)
    plan?.subActionToSubPlan?.set(subActionType, updatedSubPlan)
}

private fun SubAgentOutput.toInteractiveElements(): List<InteractiveElement> = when (this) {
    is SubAgentOutput.StayRecommendation -> hotelSuggestions
    is SubAgentOutput.TransitGeneration -> listOf(route)
    is SubAgentOutput.RestaurantSuggestions -> restaurants
    is SubAgentOutput.AttractionSuggestions -> attraction
}

private fun AgentEnum.toSubActionType(): SubActionType? = when (this) {
    AgentEnum.STAY_AGENT -> SubActionType.STAY_SUGGESTIONS
    AgentEnum.TRANSIT_AGENT -> SubActionType.TRANSIT_SUGGESTIONS
    AgentEnum.ATTRACTION_AGENT -> SubActionType.ATTRACTION_SUGGESTIONS
    AgentEnum.RESTAURANT_AGENT -> SubActionType.FOOD_SUGGESTIONS
}
