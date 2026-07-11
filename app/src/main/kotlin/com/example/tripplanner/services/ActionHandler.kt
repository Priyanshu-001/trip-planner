package com.example.tripplanner.services

import com.example.tripplanner.TripSession

interface ActionHandler {
    suspend fun perform(tripSession: TripSession): TripSession
    fun getAction(): ActionRegistry.Action
}