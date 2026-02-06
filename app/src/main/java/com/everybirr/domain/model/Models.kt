package com.everybirr.domain.model

import java.math.BigDecimal

data class BudgetMonth(
    val id: Long,
    val monthKey: String,
    val readOnly: Boolean
)

data class IncomeSource(
    val id: Long,
    val monthId: Long,
    val name: String,
    val amount: BigDecimal
)

data class Category(
    val id: Long,
    val monthId: Long,
    val name: String,
    val localizedName: String?,
    val icon: String,
    val colorHex: Long,
    val limit: BigDecimal,
    val hidden: Boolean
)

data class Expense(
    val id: Long,
    val monthId: Long,
    val categoryId: Long,
    val amount: BigDecimal,
    val memo: String,
    val createdAt: Long
)

data class CategoryBudget(
    val category: Category,
    val spent: BigDecimal,
    val available: BigDecimal
)

data class MonthSummary(
    val totalIncome: BigDecimal,
    val totalSpent: BigDecimal,
    val totalRemaining: BigDecimal,
    val savingsRate: BigDecimal
)
