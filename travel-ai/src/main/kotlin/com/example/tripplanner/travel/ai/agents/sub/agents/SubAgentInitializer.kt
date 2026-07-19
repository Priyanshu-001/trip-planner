package com.example.tripplanner.travel.ai.agents.sub.agents

import com.example.tripplanner.gmaps.tools.AttractionSearchTool
import com.example.tripplanner.gmaps.tools.HotelSearchTool
import com.example.tripplanner.gmaps.tools.RestaurantSearchTool
import com.example.tripplanner.gmaps.tools.RouteSearchTool
import com.example.tripplanner.gmaps.tools.SubAgentTools
import com.example.tripplanner.travel.ai.agents.AgentEnum
import com.example.tripplanner.travel.ai.agents.config.PromptLoader
import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SubAgentInitializer() {


    @Bean
    fun loadSubAgents(
        builder: ChatClient.Builder,
        promptLoader: PromptLoader,
        toolList: List<SubAgentTools>
    ): List<SubAgent<*>>{
        val toolListByClass = toolList.associateBy { it::class }
        return listOf(
            SubAgent(
                chatClientBuilder = builder,
                systemPrompt = promptLoader.load("transit_agent_system_prompt.st"),
                userPrompt =  """
                Goal:
                {goal}

                User Requirements:
                {requirements}

                Additional Context:
                {context}

                Determine the best transportation recommendation.
                Use the Route Search Tool if needed.
                """,
                tools = listOf(toolListByClass[RouteSearchTool::class]!!),
                agentType = AgentEnum.TRANSIT_AGENT,
                outputType = SubAgentOutput.TransitGeneration::class.java
            ),
            SubAgent(
                chatClientBuilder = builder,
                systemPrompt = promptLoader.load("stay_agent_system_prompt.st"),
                userPrompt =  """
                Goal:
                {goal}

                User Requirements:
                {requirements}

                Additional Context:
                {context}

                Based on the above information, recommend the best hotels for this trip.
                """,
                tools = listOf(toolListByClass[HotelSearchTool::class]!!),
                agentType = AgentEnum.STAY_AGENT,
                outputType = SubAgentOutput.StayRecommendation::class.java
            ),
            SubAgent( chatClientBuilder = builder,
                systemPrompt = promptLoader.load("attraction_agent_system_prompt.st"),
                userPrompt =  """
                Goal:
                {goal}

                User Requirements:
                {requirements}

                Additional Context:
                {context}

                Based on the above information, recommend the best attractions or places to visit during the trip..
                """,
                tools = listOf(toolListByClass[AttractionSearchTool::class]!!),
                agentType = AgentEnum.ATTRACTION_AGENT,
                outputType = SubAgentOutput.AttractionSuggestions::class.java),
        SubAgent( chatClientBuilder = builder,
            systemPrompt = promptLoader.load("attraction_agent_system_prompt.st"),
            userPrompt =  """
                Goal:
                {goal}

                User Requirements:
                {requirements}

                Additional Context:
                {context}

                Based on the above information, recommend the best places to eat.
                """,
            tools = listOf(toolListByClass[RestaurantSearchTool::class]!!),
            agentType = AgentEnum.RESTAURANT_AGENT,
            outputType = SubAgentOutput.RestaurantSuggestions::class.java)
        )
    }
}
