package com.everybirr

import com.everybirr.core.BudgetMath
import com.everybirr.core.OverspendingState
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class BudgetMathTest {
    @Test
    fun available_isBudgetedMinusSpent() {
        assertEquals(BigDecimal("70.00"), BudgetMath.available(BigDecimal("100.00"), BigDecimal("30.00")))
    }

    @Test
    fun overspending_detectionWorks() {
        assertEquals(OverspendingState.WARNING, BudgetMath.overspendingState(BigDecimal("100"), BigDecimal("90")))
        assertEquals(OverspendingState.EXCEEDED, BudgetMath.overspendingState(BigDecimal("100"), BigDecimal("100")))
    }
}
