package com.everybirr.domain.usecase

import com.everybirr.domain.model.Category
import com.everybirr.domain.model.Expense
import com.everybirr.domain.repository.BudgetRepository
import java.math.BigDecimal
import javax.inject.Inject

class CreateMonthUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(monthKey: String, copyFromMonthId: Long?) =
        repository.createMonth(monthKey, copyFromMonthId)
}

class AddIncomeUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(monthId: Long, name: String, amount: BigDecimal) =
        repository.addIncome(monthId, name, amount)
}

class AllocateCategoryUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(category: Category) = repository.allocateCategory(category)
}

class AddExpenseUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(expense: Expense) = repository.addExpense(expense)
}
