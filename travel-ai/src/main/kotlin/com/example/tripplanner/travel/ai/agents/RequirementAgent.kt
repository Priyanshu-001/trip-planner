package com.example.tripplanner.travel.ai.agents

import models.AgentRequirementDiscoveryRequest
import models.AgentRequirementDiscoveryResponse
import models.AgentRequirementExtractionRequest
import models.AgentRequirementExtractionResponse
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class RequirementAgent(
    private val chatClientBuilder: ChatClient.Builder,
    @Value("classpath:prompts/requirement_agent_system_prompt.st")
    private val systemPromptResource: Resource
) {
    private val chatClient = requirementAgentChatClient()

    fun getMoreQuestionsIfNeeded(req: AgentRequirementDiscoveryRequest): AgentRequirementDiscoveryResponse? {
        val template = """
       DISCOVER_REQUIREMENTS for the given context - {context}
    """.trimIndent()
        return chatClient
            .prompt()
            .user { it.text(template).param("context", req.toString()) }
            .call()
            .entity(AgentRequirementDiscoveryResponse::class.java)

    }

    fun extractRequirementsFromQuestions(agentRequirementExtractionRequest: AgentRequirementExtractionRequest): AgentRequirementExtractionResponse? {

        val template = """
       EXT_REQUIREMENT for the given context - {context}
    """.trimIndent()
        return chatClient
            .prompt()
            .user { it.text(template).param("context", agentRequirementExtractionRequest.toString()) }
            .call()
            .entity(AgentRequirementExtractionResponse::class.java)
    }


    private fun requirementAgentChatClient(): ChatClient {
        val systemPrompt =
            systemPromptResource.inputStream
                .bufferedReader()
                .use { it.readText() }

        return chatClientBuilder
            .defaultSystem(systemPrompt)
            .build()
    }
}