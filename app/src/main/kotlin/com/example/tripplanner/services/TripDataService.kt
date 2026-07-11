package com.example.tripplanner.services

import com.example.tripplanner.TripSession
import com.example.tripplanner.toBo
import com.example.tripplanner.toEntity
import com.example.tripplanner.persistence.reposistory.SpringDataTripSessionRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TripSessionDataServiceImpl(
    private val repository: SpringDataTripSessionRepository,
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
}