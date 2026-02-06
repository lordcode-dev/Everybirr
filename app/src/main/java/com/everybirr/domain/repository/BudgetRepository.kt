package com.everybirr.domain.repository

import com.everybirr.domain.model.BudgetMonth
import com.everybirr.domain.model.Category
import com.everybirr.domain.model.CategoryBudget
import com.everybirr.domain.model.Expense
import com.everybirr.domain.model.IncomeSource
import com.everybirr.domain.model.MonthSummary
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface BudgetRepository {
    fun activeMonth(): Flow<BudgetMonth?>
    fun monthSummary(monthId: Long): Flow<MonthSummary>
    fun categoryBudgets(monthId: Long): Flow<List<CategoryBudget>>
    fun expenses(categoryId: Long): Flow<List<Expense>>

    suspend fun createMonth(monthKey: String, copyFromMonthId: Long? = null): Long
    suspend fun setActiveMonth(monthId: Long)
    suspend fun addIncome(monthId: Long, name: String, amount: BigDecimal)
    suspend fun allocateCategory(category: Category)
    suspend fun addExpense(expense: Expense)
    suspend fun setMonthReadOnly(monthId: Long, readOnly: Boolean)

    suspend fun exportCsv(monthId: Long): String
    suspend fun importDefaultsIfNeeded(monthId: Long)
}
