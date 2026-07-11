package com.example.tripplanner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TripPlannerApplication

fun main(args: Array<String>) {
    runApplication<TripPlannerApplication>(*args)
}