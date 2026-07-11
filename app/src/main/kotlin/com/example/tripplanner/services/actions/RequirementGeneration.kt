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
import org.springframework.stereotype.Service

@Service
class RequirementGeneration(
    val requirementsAgent: RequirementAgent, val tripSessionDataServiceImpl: TripSessionDataServiceImpl
) : ActionHandler {
    override suspend fun perform(tripSession: TripSession): TripSession {
        extractAndEnrichRequirement(tripSession)
        generateQuestionsIfNeeded(tripSession)
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
}

private fun TripSession.ingestRequirements(requirements: Requirements?) {
    requirements ?: return

    this.requirements.apply {
        requirements.destination?.let { destination = it }
        requirements.source?.let { source = it }

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

private fun TripSession.Requirements.toDTO(): Requirements {
    return Requirements(
        destination = this.destination,
        source = this.source,
        tripDuration = Requirements.Duration(
            this.tripDuration?.value ?: 0, this.tripDuration?.unit?.toDTO() ?: Requirements.Duration.Unit.HOURS
        ),
        numberOfTravelers = this.numberOfTravelers,
        includePets = this.includePets,
        specialRequests = this.specialRequests,
        interests = this.interests
    )
}

private fun TripSession.Requirements.Duration.Unit.toDTO(): Requirements.Duration.Unit {
    return when (this) {
        TripSession.Requirements.Duration.Unit.DAYS -> Requirements.Duration.Unit.DAYS
        TripSession.Requirements.Duration.Unit.HOURS -> Requirements.Duration.Unit.HOURS
    }
}

private fun FollowQuestion.toBO(): TripSession.FollowUpQuestion = TripSession.FollowUpQuestion(
    question = this.question, answerChoices = this.answerChoices
)