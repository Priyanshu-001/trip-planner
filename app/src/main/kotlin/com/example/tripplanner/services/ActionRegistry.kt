package com.example.tripplanner.services

import org.springframework.stereotype.Service

@Service
class ActionRegistry(val actionHandlerHandlers: List<ActionHandler>) {
    private val actionHandlerMap = actionHandlerHandlers.associateBy { it.getAction() }

    fun getActionHandler(action: Action): ActionHandler {
       return actionHandlerMap[action]!!
    }

    enum class Action {
        GENERATE_REQUIREMENTS,
        PLAN_TRIP
    }
}