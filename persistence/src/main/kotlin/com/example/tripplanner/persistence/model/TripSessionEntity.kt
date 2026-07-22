package com.example.tripplanner.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document("trip_sessions")
data class TripSessionEntity(

    @Id
    val id: String = UUID.randomUUID().toString(),

    var initialPrompt: String,

    var status: Status = Status.INITIALIZED,

    var requirements: Requirements = Requirements(),

    var followUpQuestions: MutableList<FollowUpQuestion> = mutableListOf(),

    val createdAt: Instant = Instant.now(),

    var updatedAt: Instant = Instant.now(),

    val plan: Plan? = null

) {


    enum class Status {
        INITIALIZED, COLLECTING_REQUIREMENTS, WAITING_FOR_USER_INPUT, READY_FOR_PLANNING, PLANNING, COMPLETED, FAILED
    }
    data class Requirements(

        var destinationCity: String? = null,

        var sourceCity: String? = null,

        var tripDuration: Duration? = null,

        var numberOfTravelers: Int? = null,

        var includePets: Boolean = false,

        var specialRequests: MutableList<String> = mutableListOf(),

        var interests: MutableList<String> = mutableListOf()

    ) {

        data class Duration(
            var value: Int = 0,
            var unit: Unit = Unit.DAYS
        ) {
            enum class Unit {
                DAYS,
                HOURS
            }
        }
    }

    data class FollowUpQuestion(

        val id: String = UUID.randomUUID().toString(),

        var question: String,

        var answerChoices: List<String>? = null,

        var answer: String? = null,

        var answerProcessed: Boolean = false
    )
    
    data class Plan(
        val planStatus: PlanStatus,
        val subActionToSubPlan: Map<String, SubPlan>? = null
    )

    data class PlanResult(
        val subPlanStatus: PlanStatus,
        val title: String? = null,
        val description: String? = null,
        var status: SubPlanStatus = SubPlanStatus.READY,
        val interactiveElements: List<String> = emptyList()
    )

    data class SubPlan(
        val planResult: PlanResult? = null,
        val subAgentInstructions: SubAgentInstructions
    )

    enum class SubPlanStatus {
        READY, PENDING, DONE
    }

    enum class PlanStatus {
        NOT_READY, READY, ALL_DONE
    }

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
}