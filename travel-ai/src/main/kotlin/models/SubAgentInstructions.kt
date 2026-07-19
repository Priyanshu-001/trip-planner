package models

data class SubAgentInstructions(
    val goal: String,
    val context: List<AgentContext> = emptyList(),
    val requirements: Requirements
)
data class AgentContext(
    val name: String,
    val value: String,
    val importanceLevel: ImportanceLevel = ImportanceLevel.MUST_HAVE
)

enum class ImportanceLevel {
    MUST_HAVE,
    GOOD_TO_HAVE
}