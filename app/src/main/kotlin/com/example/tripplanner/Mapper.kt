package com.example.tripplanner

import com.example.tripplanner.persistence.model.TripSessionEntity
import java.time.Instant

fun TripSession.toEntity(): TripSessionEntity =
    TripSessionEntity(
        id = id,
        initialPrompt = initialPrompt,
        status = status.toEntity(),
        requirements = requirements.toEntity(),
        followUpQuestions = followUpQuestions.map { it.toEntity() }.toMutableList(),
        updatedAt = Instant.now()
    )

fun TripSessionEntity.toBo(): TripSession =
    TripSession(
        id = id,
        initialPrompt = initialPrompt,
        status = status.toBo(),
        requirements = requirements.toBo(),
        followUpQuestions = followUpQuestions.map { it.toBo() }.toMutableList()
    )

private fun TripSession.Status.toEntity() =
    when (this) {
        TripSession.Status.WAITING_FOR_USER_INPUT -> TripSessionEntity.Status.WAITING_FOR_USER_INPUT
        TripSession.Status.READY_FOR_PLANNING -> TripSessionEntity.Status.READY_FOR_PLANNING
        TripSession.Status.INITIALIZED ->
            TripSessionEntity.Status.COLLECTING_REQUIREMENTS
        TripSession.Status.COLLECTING_REQUIREMENTS ->
            TripSessionEntity.Status.COLLECTING_REQUIREMENTS
        TripSession.Status.PLANNING ->
            TripSessionEntity.Status.PLANNING
        TripSession.Status.COMPLETED ->
            TripSessionEntity.Status.COMPLETED
        TripSession.Status.FAILED ->
            TripSessionEntity.Status.FAILED
    }

private fun TripSessionEntity.Status.toBo() =
    when (this) {
        TripSessionEntity.Status.COLLECTING_REQUIREMENTS ->
            TripSession.Status.COLLECTING_REQUIREMENTS
        TripSessionEntity.Status.PLANNING ->
            TripSession.Status.PLANNING
        TripSessionEntity.Status.COMPLETED ->
            TripSession.Status.COMPLETED
        TripSessionEntity.Status.FAILED ->
            TripSession.Status.FAILED

        TripSessionEntity.Status.INITIALIZED -> TripSession.Status.INITIALIZED
        TripSessionEntity.Status.WAITING_FOR_USER_INPUT -> TripSession.Status.WAITING_FOR_USER_INPUT
        TripSessionEntity.Status.READY_FOR_PLANNING -> TripSession.Status.READY_FOR_PLANNING
    }

private fun TripSession.Requirements.toEntity() =
    TripSessionEntity.Requirements(
        destinationCity = destinationCity,
        sourceCity = sourceCity,
        tripDuration = tripDuration?.toEntity(),
        numberOfTravelers = numberOfTravelers,
        includePets = includePets,
        specialRequests = specialRequests.toMutableList(),
        interests = interests.toMutableList()
    )

private fun TripSessionEntity.Requirements.toBo() =
    TripSession.Requirements(
        destinationCity = destinationCity,
        sourceCity = sourceCity,
        tripDuration = tripDuration?.toBo(),
        numberOfTravelers = numberOfTravelers,
        includePets = includePets,
        specialRequests = specialRequests.toMutableList(),
        interests = interests.toMutableList()
    )

private fun TripSession.Requirements.Duration.toEntity() =
    TripSessionEntity.Requirements.Duration(
        value = value,
        unit = unit.toEntity()
    )

private fun TripSessionEntity.Requirements.Duration.toBo() =
    TripSession.Requirements.Duration(
        value = value,
        unit = unit.toBo()
    )

private fun TripSession.Requirements.Duration.Unit.toEntity() =
    when (this) {
        TripSession.Requirements.Duration.Unit.DAYS ->
            TripSessionEntity.Requirements.Duration.Unit.DAYS
        TripSession.Requirements.Duration.Unit.HOURS ->
            TripSessionEntity.Requirements.Duration.Unit.HOURS
    }

private fun TripSessionEntity.Requirements.Duration.Unit.toBo() =
    when (this) {
        TripSessionEntity.Requirements.Duration.Unit.DAYS ->
            TripSession.Requirements.Duration.Unit.DAYS
        TripSessionEntity.Requirements.Duration.Unit.HOURS ->
            TripSession.Requirements.Duration.Unit.HOURS
    }

private fun TripSession.FollowUpQuestion.toEntity() =
    TripSessionEntity.FollowUpQuestion(
        id = id,
        question = question,
        answerChoices = answerChoices,
        answer = answer,
        answerProcessed = answerProcessed
    )

private fun TripSessionEntity.FollowUpQuestion.toBo() =
    TripSession.FollowUpQuestion(
        id = id,
        question = question,
        answerChoices = answerChoices,
        answer = answer,
        answerProcessed = answerProcessed
    )