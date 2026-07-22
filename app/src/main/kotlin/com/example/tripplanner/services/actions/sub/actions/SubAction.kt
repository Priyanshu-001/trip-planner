package com.example.tripplanner.services.actions.sub.actions

import com.example.tripplanner.TripSession

interface SubAction {

   suspend fun perform(tripSession: TripSession, subActionType: SubActionType)

}

enum class SubActionType {
    STAY_SUGGESTIONS,
    TRANSIT_SUGGESTIONS,
    ATTRACTION_SUGGESTIONS,
    FOOD_SUGGESTIONS
}