package com.example.tripplanner.services.actions

import com.example.tripplanner.TripSession
import com.example.tripplanner.services.actions.sub.actions.SubActionType
import com.example.tripplanner.travel.ai.agents.AgentEnum
import com.example.tripplanner.travel.ai.agents.AgentInstructions
import com.example.tripplanner.travel.ai.agents.OrchestrationResult
import com.example.tripplanner.travel.ai.agents.sub.agents.InteractiveElement
import com.example.tripplanner.travel.ai.agents.sub.agents.SubAgentOutput
import models.AgentContext
import models.ImportanceLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CreatePlanTest {

    @Test
    fun `ingest maps orchestration instructions into the trip session plan`() {
        val tripSession = TripSession(initialPrompt = "Plan my weekend")

        val orchestrationResult = OrchestrationResult(
            nextAgentsInstructions = listOf(
                AgentInstructions(
                    agent = AgentEnum.STAY_AGENT,
                    goal = "Find a hotel",
                    context = listOf(AgentContext("budget", "moderate", ImportanceLevel.MUST_HAVE))
                ),
                AgentInstructions(
                    agent = AgentEnum.TRANSIT_AGENT,
                    goal = "Plan airport transfers",
                    context = emptyList()
                )
            )
        )

        tripSession.ingest(orchestrationResult)

        val plan = tripSession.plan
        assertNotNull(plan)
        assertEquals(TripSession.PlanStatus.READY, plan!!.planStatus)
        assertNotNull(plan.subActionToSubPlan)
        assertEquals(2, plan.subActionToSubPlan!!.size)
        assertTrue(plan.subActionToSubPlan!!.containsKey(SubActionType.STAY_SUGGESTIONS))
        assertTrue(plan.subActionToSubPlan!!.containsKey(SubActionType.TRANSIT_SUGGESTIONS))

        val stayPlan = plan.subActionToSubPlan!![SubActionType.STAY_SUGGESTIONS]
        assertNotNull(stayPlan)
        assertEquals("Find a hotel", stayPlan!!.subAgentInstructions.goal)
        assertEquals(1, stayPlan.subAgentInstructions.context.size)
        assertEquals("budget", stayPlan.subAgentInstructions.context.first().name)
        assertEquals("moderate", stayPlan.subAgentInstructions.context.first().value)
    }

    @Test
    fun `ingest stores sub agent output into the matching sub plan result`() {
        val tripSession = TripSession(initialPrompt = "Plan my weekend")
        tripSession.ingest(
            OrchestrationResult(
                nextAgentsInstructions = listOf(
                    AgentInstructions(
                        agent = AgentEnum.STAY_AGENT,
                        goal = "Find a hotel",
                        context = emptyList()
                    )
                )
            )
        )

        val subAgentOutput = SubAgentOutput.StayRecommendation(
            oneLineExplanation = "Stay near the waterfront",
            detailedExplanation = "Quiet hotel with easy access to attractions",
            hotelSuggestions = listOf(
                InteractiveElement.Hotel(
                    id = "hotel-1",
                    title = "Harbor View",
                    rating = 4.8,
                    address = "1 Main St",
                    mapsUrl = "https://maps.example/hotel",
                    imageUrl = null
                )
            )
        )

        tripSession.ingest(subAgentOutput, SubActionType.STAY_SUGGESTIONS)

        val stayPlan = tripSession.plan!!.subActionToSubPlan!![SubActionType.STAY_SUGGESTIONS]
        assertNotNull(stayPlan)
        assertNotNull(stayPlan!!.planResult)
        assertEquals("Stay near the waterfront", stayPlan.planResult!!.title)
        assertEquals("Quiet hotel with easy access to attractions", stayPlan.planResult!!.description)
        assertEquals(1, stayPlan.planResult!!.interactiveElements.size)
        assertTrue(stayPlan.planResult!!.interactiveElements.first() is InteractiveElement.Hotel)
    }
}
