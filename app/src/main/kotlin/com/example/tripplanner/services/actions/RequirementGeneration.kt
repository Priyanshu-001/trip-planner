package com.example.tripplanner.services.actions

import models.AgentRequirementExtractionRequest
import com.example.tripplanner.TripSession
import com.example.tripplanner.services.ActionHandler
import com.example.tripplanner.services.ActionRegistry
import com.example.tripplanner.services.TripSessionDataServiceImpl
import com.example.tripplanner.travel.ai.agents.RequirementAgent
import models.AgentRequirementDiscoveryRequest
import models.FollowQuestion
import models.Requirements
import models.UserAnsweredQuestions
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RequirementGeneration(
    val requirementsAgent: RequirementAgent, val tripSessionDataServiceImpl: TripSessionDataServiceImpl
) : ActionHandler {
    override suspend fun perform(tripSession: TripSession): TripSession {
        logger.info("being requirement gen")
        extractAndEnrichRequirement(tripSession)
        logger.info("being question gen")
        generateQuestionsIfNeeded(tripSession)
        logger.info("next stage")
        return tripSessionDataServiceImpl.save(tripSession)
    }

    private fun generateQuestionsIfNeeded(tripSession: TripSession) {

        val response = requirementsAgent.getMoreQuestionsIfNeeded(
            AgentRequirementDiscoveryRequest(
                tripSession.requirements.toDTO(), tripSession.initialPrompt
            )
        )
        tripSession.ingestQuestions(response?.followUpQuestions)
        tripSession.status =
            if (response?.followUpQuestions.isNullOrEmpty()) TripSession.Status.READY_FOR_PLANNING else TripSession.Status.WAITING_FOR_USER_INPUT
    }

    private suspend fun extractAndEnrichRequirement(tripSession: TripSession) {
        tripSession.status = TripSession.Status.COLLECTING_REQUIREMENTS
        tripSessionDataServiceImpl.save(tripSession)
        val agentGeneratedRequirements = requirementsAgent.extractRequirementsFromQuestions(
            AgentRequirementExtractionRequest(
                tripSession.initialPrompt, tripSession.followUpQuestions.toDTO()
            )
        )
        if (agentGeneratedRequirements == null || !agentGeneratedRequirements.inconsistenciesWithExistingData.isNullOrEmpty()) {
            throw RuntimeException()
        }
        tripSession.ingestRequirements(agentGeneratedRequirements.requirements)
    }

    override fun getAction() = ActionRegistry.Action.GENERATE_REQUIREMENTS

    companion object {
        private val logger = LoggerFactory.getLogger(RequirementGeneration::class.java)
    }
}

private fun TripSession.ingestRequirements(requirements: Requirements?) {
    requirements ?: return

    this.requirements.apply {
        requirements.destinationCity?.let { destinationCity = it }
        requirements.sourceCity?.let { sourceCity = it }

        requirements.tripDuration?.let {
            tripDuration = TripSession.Requirements.Duration(
                value = it.value, unit = when (it.unit) {
                    Requirements.Duration.Unit.DAYS -> TripSession.Requirements.Duration.Unit.DAYS

                    Requirements.Duration.Unit.HOURS -> TripSession.Requirements.Duration.Unit.HOURS
                }
            )
        }

        requirements.numberOfTravelers?.let { numberOfTravelers = it }
        requirements.includePets?.let { includePets = it }
        requirements.specialRequests?.let { specialRequests = it as MutableList<String> }
        requirements.interests?.let { interests = it as MutableList<String> }
    }
}

private fun TripSession.ingestQuestions(followUpQuestions: List<FollowQuestion>?) {
    this.followUpQuestions.forEach { it.answerProcessed = true }
    this.followUpQuestions.addAll(followUpQuestions?.map { it.toBO() } ?: emptyList())
}


private fun List<TripSession.FollowUpQuestion>.toDTO(): List<UserAnsweredQuestions> {
    return this.map { it.toDTO() }
}

private fun TripSession.FollowUpQuestion.toDTO(): UserAnsweredQuestions {
    return UserAnsweredQuestions(this.question, this.answer ?: "")
}

private fun FollowQuestion.toBO(): TripSession.FollowUpQuestion = TripSession.FollowUpQuestion(
    question = this.question, answerChoices = this.answerChoices
)