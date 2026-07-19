package com.example.tripplanner.travel.ai.agents

import models.AgentContext
import models.AgentRequirementExtractionResponse
import models.QueryRejection
import models.Requirements
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class OrchestratorAgent(
    private val chatClientBuilder: ChatClient.Builder,
    @Value("classpath:prompts/requirement_agent_system_prompt.st")
    private val systemPromptResource: Resource
) {
    private val chatClient = getChatClient()

    fun orchestrateTrip(orchestrateTripRequest: OrchestrateTripRequest): OrchestrationResult? {

        val template = """
     Original User Request {prompt}
    Structured Requirements
    {requirements}
    Determine which agents should execute.
    Return only JSON.
    """.trimIndent()

      return  chatClient
            .prompt()
            .user { it.text(template).param("prompt", orchestrateTripRequest.initPrompt)
                .param("requirements", orchestrateTripRequest.requirements.toString()) }
            .call()
            .entity(OrchestrationResult::class.java)


    }

    private fun getChatClient(): ChatClient {
        val systemPrompt =
            systemPromptResource.inputStream
                .bufferedReader()
                .use { it.readText() }

        return chatClientBuilder
            .defaultSystem(systemPrompt)
            .build()
    }
}

data class OrchestrationResult(
    val nextAgentsInstructions: List<AgentInstructions>,
    val queryRejection: QueryRejection? = null
)

data class AgentInstructions(
    val agent: AgentEnum,
    val goal: String,
    val context: List<AgentContext> = emptyList()
)

enum class AgentEnum {
    STAY_AGENT,
    TRANSIT_AGENT,
    RESTAURANT_AGENT,
    ATTRACTION_AGENT
}


data class OrchestrateTripRequest(
    val requirements: Requirements,
    val initPrompt: String,
)