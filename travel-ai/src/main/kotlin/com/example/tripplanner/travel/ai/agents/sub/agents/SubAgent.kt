package com.example.tripplanner.travel.ai.agents.sub.agents

import com.example.tripplanner.gmaps.tools.SubAgentTools
import com.example.tripplanner.travel.ai.agents.AgentEnum
import models.SubAgentInstructions
import org.springframework.ai.chat.client.ChatClient

class SubAgent<T: SubAgentOutput>(private val chatClientBuilder: ChatClient.Builder,
                       private val systemPrompt: String,
                        private val userPrompt: String,
                        private val tools: List<SubAgentTools> = emptyList(),
                        private val agentType: AgentEnum,
                        private val outputType: Class<T>
) {

    fun perform(subAgentInstructions: SubAgentInstructions): T {
        return chatClient.prompt()
            .user { it.text(userPrompt.trimIndent())
                .param("requirements", subAgentInstructions.requirements)
                .param("context", subAgentInstructions.context)
                .param("goal", subAgentInstructions.goal)
            }
            .call()
            .entity(outputType)!!
    }
    fun getType() = agentType
    private fun buildChatClient(): ChatClient {
        return chatClientBuilder
            .defaultSystem(systemPrompt)
            .defaultTools(tools)
            .build()
    }

     val chatClient: ChatClient = buildChatClient()

}

sealed interface SubAgentOutput {
    val oneLineExplanation: String
    val detailedExplanation: String

    data class StayRecommendation(
        override val oneLineExplanation: String,
        override val detailedExplanation: String,
        val hotelSuggestions: List<InteractiveElement.Hotel>,
    ) : SubAgentOutput

    data class TransitGeneration(
        override val oneLineExplanation: String,
        override val detailedExplanation: String,
        val route: InteractiveElement.Route,
    ) : SubAgentOutput

    data class RestaurantSuggestions(
        override val oneLineExplanation: String,
        override val detailedExplanation: String,
        val restaurants: List<InteractiveElement.Restaurant>,
    ): SubAgentOutput

    data class AttractionSuggestions(
        override val oneLineExplanation: String,
        override val detailedExplanation: String,
        val attraction: List<InteractiveElement.Attraction>,
    ): SubAgentOutput
}

sealed interface InteractiveElement {

    val id: String
    val title: String

    data class Hotel(
        override val id: String,
        override val title: String,
        val rating: Double?,
        val address: String?,
        val mapsUrl: String?,
        val imageUrl: String?
    ) : InteractiveElement

    data class Attraction(
        override val id: String,
        override val title: String,
        val description: String,
        val mapUrl: String
    ) : InteractiveElement

    data class Restaurant(
        override val id: String,
        override val title: String,
        val description: String,
        val mapUrl: String
    ) : InteractiveElement

    data class Route(
        override val id: String,
        override val title: String,
        val duration: String?,
        val distance: String?,
        val mode: Mode,
        val mapsUrl: String?
    ) : InteractiveElement {

        enum class Mode {
            WALK,
            CAR,
            TRAIN,
            PLANE
        }
    }
}