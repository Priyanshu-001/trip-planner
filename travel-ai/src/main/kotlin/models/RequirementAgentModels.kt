package models

data class AgentRequirementExtractionResponse(
    val requirements: Requirements?,
    val queryRejection: QueryRejection? = null,
    val inconsistenciesWithExistingData: List<String>? = emptyList(),
    val missingDetails: List<String>? = null,
    val requirementsConversionSuccessful: Boolean = false,
)

data class QueryRejection(
    val rejectionCode: RejectionCode,
    val rejectionReason: String
)

enum class RejectionCode {
    GUARD_RAIL_PROTECTION, LOOP_EXCEEDED_THRESHOLD
}

data class AgentRequirementExtractionRequest(
    val initialPrompt: String,
    val userAnsweredQuestions: List<UserAnsweredQuestions>? = emptyList()
)

data class UserAnsweredQuestions(
    val question: String,
    val answer: String
)

data class Requirements(
    val destinationCity: String?,
    val sourceCity: String?,
    val tripDuration: Duration?,
    val numberOfTravelers: Int? = 0,
    val includePets: Boolean? = false,
    val specialRequests: List<String>? = emptyList(),
    val interests: List<String>? = emptyList(),

    ) {
    data class Duration(

        val value: Int,
        val unit: Unit

    ) {
        enum class Unit {
            DAYS, HOURS
        }
    }

}

data class AgentRequirementDiscoveryResponse(
    val followUpQuestions: List<FollowQuestion>? = null,
    val queryRejection: QueryRejection? = null,
)

data class AgentRequirementDiscoveryRequest(
    val requirements:Requirements,
    val initialPrompt: String,
    val questionLoopTimes: Int = 0
)

data class FollowQuestion(
    val question: String,
    val answerChoices: List<String>?,
)
