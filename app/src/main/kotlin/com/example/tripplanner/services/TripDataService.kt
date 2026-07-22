package com.example.tripplanner.services

import com.example.tripplanner.TripSession
import com.example.tripplanner.persistence.model.TripSessionEntity
import com.example.tripplanner.toBo
import com.example.tripplanner.toEntity
import com.example.tripplanner.persistence.reposistory.SpringDataTripSessionRepository
import com.example.tripplanner.services.actions.sub.actions.SubActionType
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

private const val _ID = "_id"

@Service
class TripSessionDataServiceImpl(
    private val repository: SpringDataTripSessionRepository,
    private val mongoTemplate: ReactiveMongoTemplate,
    @Value("\${spring.data.mongodb.uri}")
    private  var something: String
)  {


    init {
        println("URL ")
        println(something)
    }

     suspend fun save(session: TripSession): TripSession {

        val entity = session.toEntity()

        val saved = repository.save(entity).awaitSingle()

        return saved.toBo()
    }

     suspend fun findById(id: String): TripSession? {

        return repository.findById(id)
                .awaitFirstOrNull()?.toBo()
    }

     suspend fun deleteById(id: String) {

        repository.deleteById(id)
            .awaitFirstOrNull()
    }

     suspend fun exists(id: String): Boolean {

        return repository.existsById(id)
            .awaitSingle()
    }
    private fun subPlanPath(subActionType: SubActionType) =
        "plan.subActionToSubPlan.${subActionType.name}.planResult"

    suspend fun markSubTaskAsPending(
        tripSession: TripSession,
        subActionType: SubActionType
    ): TripSession {

        val path = subPlanPath(subActionType)

        val statusKey = getStatusPath(path)
        val query = Query.query(
            Criteria.where(_ID).`is`(tripSession.id)
                .and(statusKey)
                .`is`(TripSession.SubPlanStatus.READY)
        )

        val update = Update()
            .set(statusKey, TripSession.SubPlanStatus.PENDING)

        mongoTemplate.updateFirst(query, update, TripSessionEntity::class.java)
            .awaitSingle()

        return findById(tripSession.id)!!
    }

    suspend fun saveSubTaskResult(
        tripSession: TripSession,
        subActionType: SubActionType
    ): TripSession {

        val planResult = tripSession.getPlanResult(subActionType)
        planResult.status = TripSession.SubPlanStatus.PENDING
        val path = subPlanPath(subActionType)
        val pathStatus = getStatusPath(path)

        val query = Query.query(
            Criteria.where(_ID).`is`(tripSession.id)
                .and(pathStatus)
                .`is`(TripSession.SubPlanStatus.PENDING)
        )

        val update = Update()
            .set(path, planResult.toEntity())

        mongoTemplate.updateFirst(query, update, TripSessionEntity::class.java)
            .awaitSingle()

        return findById(tripSession.id)!!
    }

    suspend fun markSubTaskAsNotStarted(
        tripSession: TripSession,
        subActionType: SubActionType
    ): TripSession {

        val path = subPlanPath(subActionType)
        val statusPath = getStatusPath(path)
        val query = Query.query(
            Criteria.where(_ID).`is`(tripSession.id)
                .and(statusPath)
                .`is`(TripSession.SubPlanStatus.PENDING)
        )

        val update = Update()
            .set(statusPath, TripSession.SubPlanStatus.READY)

        mongoTemplate.updateFirst(query, update, TripSessionEntity::class.java)
            .awaitSingle()

        return findById(tripSession.id)!!
    }

    private fun getStatusPath(path: String) = "$path.status"

}