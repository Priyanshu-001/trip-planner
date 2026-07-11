package com.example.tripplanner.persistence.configs

import com.mongodb.ConnectionString
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories(basePackages = ["com.example.tripplanner.persistence"])
class MongoConfig {

    @Value("\${spring.data.mongodb.uri}")
    lateinit var uri: String

    @Bean
    fun mongoClient(): MongoClient =
        MongoClients.create(uri)

    @Bean
    fun reactiveMongoDatabaseFactory(
        mongoClient: MongoClient
    ): ReactiveMongoDatabaseFactory {
        val connectionString = ConnectionString(uri)
        return SimpleReactiveMongoDatabaseFactory(
            mongoClient,
            connectionString.database!!
        )
    }
}