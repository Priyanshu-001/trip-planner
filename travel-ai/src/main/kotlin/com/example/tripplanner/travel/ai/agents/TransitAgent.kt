package com.example.tripplanner.travel.ai.agents

import models.InteractiveElement
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource

class TransitAgent( private val chatClientBuilder: ChatClient.Builder,
                    @Value("classpath:prompts/requirement_agent_system_prompt.st")
                    private val systemPromptResource: Resource) {


    data class TransitGeneration(
        val oneLineExplanation : String,
        val detailedExplanation: String,
        val route: InteractiveElement.Route,
        )


}