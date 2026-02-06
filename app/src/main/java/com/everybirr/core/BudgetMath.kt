package com.everybirr.core

import java.math.BigDecimal

object BudgetMath {
    fun available(budgeted: BigDecimal, spent: BigDecimal): BigDecimal = budgeted.subtract(spent)

    fun overspendingState(budgeted: BigDecimal, spent: BigDecimal): OverspendingState {
        if (budgeted <= BigDecimal.ZERO) return if (spent > BigDecimal.ZERO) OverspendingState.EXCEEDED else OverspendingState.SAFE
        val ratio = spent.divide(budgeted, 4, java.math.RoundingMode.HALF_UP)
        return when {
            ratio >= BigDecimal.ONE -> OverspendingState.EXCEEDED
            ratio >= BigDecimal("0.85") -> OverspendingState.WARNING
            else -> OverspendingState.SAFE
        }
    }
}

enum class OverspendingState { SAFE, WARNING, EXCEEDED }
