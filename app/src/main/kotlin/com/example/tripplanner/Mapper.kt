package com.example.tripplanner

import com.example.tripplanner.persistence.model.TripSessionEntity
import com.example.tripplanner.services.actions.sub.actions.SubActionType
import java.time.Instant
import java.util.EnumMap

fun TripSession.toEntity(): TripSessionEntity =
    TripSessionEntity(
        id = id,
        initialPrompt = initialPrompt,
        status = status.toEntity(),
        requirements = requirements.toEntity(),
        followUpQuestions = followUpQuestions.map { it.toEntity() }.toMutableList(),
        updatedAt = Instant.now(),
        plan = plan?.toEntity()
    )

fun TripSessionEntity.toBo(): TripSession =
    TripSession(
        id = id,
        initialPrompt = initialPrompt,
        status = status.toBo(),
        requirements = requirements.toBo(),
        followUpQuestions = followUpQuestions.map { it.toBo() }.toMutableList(),
        plan = plan?.toBo()
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

fun TripSession.Plan.toEntity(): TripSessionEntity.Plan =
    TripSessionEntity.Plan(
        planStatus = planStatus.toEntity(),
        subActionToSubPlan = subActionToSubPlan?.let { m: EnumMap<SubActionType, TripSession.SubPlan> ->
            val result: MutableMap<String, TripSessionEntity.SubPlan> = mutableMapOf()
            for ((k, v) in m) {
                result[k.name] = v.toEntity()
            }
            result
        }
    )

fun TripSessionEntity.Plan.toBo(): TripSession.Plan =
    TripSession.Plan(
        planStatus = planStatus.toBo(),
        subActionToSubPlan = subActionToSubPlan?.let { m: Map<String, TripSessionEntity.SubPlan> ->
            val em = EnumMap<SubActionType, TripSession.SubPlan>(SubActionType::class.java)
            for ((k, v) in m) {
                em[SubActionType.valueOf(k)] = v.toBo()
            }
            em
        }
    )

fun TripSession.PlanResult.toEntity(): TripSessionEntity.PlanResult =
    TripSessionEntity.PlanResult(
        subPlanStatus = subPlanStatus.toEntity(),
        title = title,
        description = description,
        status = status.toEntity(),
        interactiveElements = interactiveElements.map { it.toString() }
    )

fun TripSessionEntity.PlanResult.toBo(): TripSession.PlanResult =
    TripSession.PlanResult(
        subPlanStatus = subPlanStatus.toBo(),
        title = title,
        description = description,
        status = status.toBo(),
        interactiveElements = emptyList()
    )

fun TripSession.SubPlan.toEntity(): TripSessionEntity.SubPlan =
    TripSessionEntity.SubPlan(
        planResult = planResult?.toEntity(),
        subAgentInstructions = subAgentInstructions.toEntity()
    )

fun TripSessionEntity.SubPlan.toBo(): TripSession.SubPlan =
    TripSession.SubPlan(
        planResult = planResult?.toBo(),
        subAgentInstructions = subAgentInstructions.toBo()
    )

private fun TripSession.PlanStatus.toEntity() =
    when (this) {
        TripSession.PlanStatus.NOT_READY -> TripSessionEntity.PlanStatus.NOT_READY
        TripSession.PlanStatus.READY -> TripSessionEntity.PlanStatus.READY
        TripSession.PlanStatus.ALL_DONE -> TripSessionEntity.PlanStatus.ALL_DONE
    }

private fun TripSessionEntity.PlanStatus.toBo() =
    when (this) {
        TripSessionEntity.PlanStatus.NOT_READY -> TripSession.PlanStatus.NOT_READY
        TripSessionEntity.PlanStatus.READY -> TripSession.PlanStatus.READY
        TripSessionEntity.PlanStatus.ALL_DONE -> TripSession.PlanStatus.ALL_DONE
    }

private fun TripSession.SubPlanStatus.toEntity() =
    when (this) {
        TripSession.SubPlanStatus.READY -> TripSessionEntity.SubPlanStatus.READY
        TripSession.SubPlanStatus.PENDING -> TripSessionEntity.SubPlanStatus.PENDING
        TripSession.SubPlanStatus.DONE -> TripSessionEntity.SubPlanStatus.DONE
    }

private fun TripSessionEntity.SubPlanStatus.toBo() =
    when (this) {
        TripSessionEntity.SubPlanStatus.READY -> TripSession.SubPlanStatus.READY
        TripSessionEntity.SubPlanStatus.PENDING -> TripSession.SubPlanStatus.PENDING
        TripSessionEntity.SubPlanStatus.DONE -> TripSession.SubPlanStatus.DONE
    }

private fun models.SubAgentInstructions.toEntity() =
    TripSessionEntity.SubAgentInstructions(
        goal = goal,
        context = context.map { TripSessionEntity.AgentContext(it.name, it.value, when (it.importanceLevel) {
            models.ImportanceLevel.MUST_HAVE -> TripSessionEntity.ImportanceLevel.MUST_HAVE
            models.ImportanceLevel.GOOD_TO_HAVE -> TripSessionEntity.ImportanceLevel.GOOD_TO_HAVE
        }) },
        requirements = TripSessionEntity.Requirements(
            destinationCity = requirements.destinationCity,
            sourceCity = requirements.sourceCity,
            tripDuration = requirements.tripDuration?.let { TripSessionEntity.Requirements.Duration(it.value, when (it.unit) {
                models.Requirements.Duration.Unit.DAYS -> TripSessionEntity.Requirements.Duration.Unit.DAYS
                models.Requirements.Duration.Unit.HOURS -> TripSessionEntity.Requirements.Duration.Unit.HOURS
            }) },
            numberOfTravelers = requirements.numberOfTravelers,
            includePets = requirements.includePets ?: false,
            specialRequests = requirements.specialRequests?.toMutableList() ?: mutableListOf(),
            interests = requirements.interests?.toMutableList() ?: mutableListOf()
        )
    )

private fun TripSessionEntity.SubAgentInstructions.toBo() =
    models.SubAgentInstructions(
        goal = goal,
        context = context.map { models.AgentContext(it.name, it.value, when (it.importanceLevel) {
            TripSessionEntity.ImportanceLevel.MUST_HAVE -> models.ImportanceLevel.MUST_HAVE
            TripSessionEntity.ImportanceLevel.GOOD_TO_HAVE -> models.ImportanceLevel.GOOD_TO_HAVE
        }) },
        requirements = models.Requirements(
            destinationCity = requirements.destinationCity,
            sourceCity = requirements.sourceCity,
            tripDuration = requirements.tripDuration?.let { models.Requirements.Duration(it.value, when (it.unit) {
                TripSessionEntity.Requirements.Duration.Unit.DAYS -> models.Requirements.Duration.Unit.DAYS
                TripSessionEntity.Requirements.Duration.Unit.HOURS -> models.Requirements.Duration.Unit.HOURS
            }) },
            numberOfTravelers = requirements.numberOfTravelers ?: 0,
            includePets = requirements.includePets,
            specialRequests = requirements.specialRequests ?: mutableListOf(),
            interests = requirements.interests ?: mutableListOf()
        )
    )