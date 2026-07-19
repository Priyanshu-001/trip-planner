package com.example.tripplanner.travel.ai.agents.config

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class PromptLoader(
    private val resourceLoader: ResourceLoader
) {
    fun load(path: String): String =
        resourceLoader
            .getResource("classpath:prompts/$path")
            .inputStream
            .bufferedReader()
            .use { it.readText() }
}