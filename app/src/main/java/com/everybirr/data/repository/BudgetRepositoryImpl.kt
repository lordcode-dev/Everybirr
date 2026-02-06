package com.everybirr.data.repository

import com.everybirr.core.BudgetMath
import com.everybirr.core.money
import com.everybirr.data.db.BudgetDao
import com.everybirr.data.db.BudgetMonthEntity
import com.everybirr.data.db.CategoryEntity
import com.everybirr.data.db.ExpenseEntity
import com.everybirr.data.db.IncomeEntity
import com.everybirr.domain.model.BudgetMonth
import com.everybirr.domain.model.Category
import com.everybirr.domain.model.CategoryBudget
import com.everybirr.domain.model.Expense
import com.everybirr.domain.model.MonthSummary
import com.everybirr.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao
) : BudgetRepository {

    override fun activeMonth(): Flow<BudgetMonth?> = dao.activeMonth().map { it?.toDomain() }

    override fun monthSummary(monthId: Long): Flow<MonthSummary> = combine(
        dao.incomes(monthId),
        dao.categories(monthId),
        dao.expensesByMonthFlow(monthId)
    ) { incomes, categories, expenses ->
        val totalIncome = incomes.sumOf { it.amount.toBigDecimal() }.money()
        val totalSpent = expenses.sumOf { it.amount.toBigDecimal() }.money()
        val totalRemaining = totalIncome.subtract(totalSpent).money()
        val savingsRate = if (totalIncome > BigDecimal.ZERO) totalRemaining.divide(totalIncome, 4, java.math.RoundingMode.HALF_UP) else BigDecimal.ZERO
        MonthSummary(totalIncome, totalSpent, totalRemaining, savingsRate)
    }

    override fun categoryBudgets(monthId: Long): Flow<List<CategoryBudget>> = combine(
        dao.categories(monthId), dao.expensesByMonthFlow(monthId)
    ) { categories, expenses ->
        categories.map { category ->
            val spent = expenses.filter { it.categoryId == category.id }.sumOf { it.amount.toBigDecimal() }.money()
            CategoryBudget(
                category = category.toDomain(),
                spent = spent,
                available = BudgetMath.available(category.limitAmount.toBigDecimal(), spent).money()
            )
        }
    }

    override fun expenses(categoryId: Long): Flow<List<Expense>> = dao.expenses(categoryId).map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun createMonth(monthKey: String, copyFromMonthId: Long?): Long {
        val monthId = dao.insertMonth(BudgetMonthEntity(monthKey = monthKey, isActive = true))
        dao.setActiveMonth(monthId)
        if (copyFromMonthId != null) {
            dao.categoriesNow(copyFromMonthId).forEach { source ->
                dao.insertCategory(source.copy(id = 0, monthId = monthId))
            }
        } else {
            importDefaultsIfNeeded(monthId)
        }
        return monthId
    }

    override suspend fun setActiveMonth(monthId: Long) = dao.setActiveMonth(monthId)

    override suspend fun addIncome(monthId: Long, name: String, amount: BigDecimal) {
        ensureMonthEditable(monthId)
        dao.insertIncome(IncomeEntity(monthId = monthId, name = name, amount = amount.money().toPlainString()))
    }

    override suspend fun allocateCategory(category: Category) {
        ensureMonthEditable(category.monthId)
        dao.insertCategory(category.toEntity())
    }

    override suspend fun addExpense(expense: Expense) {
        ensureMonthEditable(expense.monthId)
        dao.insertExpense(expense.toEntity())
    }

    override suspend fun setMonthReadOnly(monthId: Long, readOnly: Boolean) = dao.setMonthReadOnly(monthId, readOnly)

    override suspend fun exportCsv(monthId: Long): String {
        val rows = dao.expensesNow(monthId)
        return buildString {
            appendLine("id,categoryId,amount,memo,createdAt")
            rows.forEach { appendLine("${it.id},${it.categoryId},${it.amount},${it.memo},${it.createdAt}") }
        }
    }

    override suspend fun importDefaultsIfNeeded(monthId: Long) {
        if (dao.categoriesNow(monthId).isNotEmpty()) return
        listOf(
            Triple("Food", "ምግብ", 0xFFE57373),
            Triple("Transport", "መጓጓዣ", 0xFF64B5F6),
            Triple("Rent", null, 0xFF9575CD),
            Triple("School", null, 0xFF81C784),
            Triple("Internet", null, 0xFF4DB6AC),
            Triple("Health", null, 0xFFFFB74D),
            Triple("Giving", null, 0xFFA1887F),
            Triple("Savings", null, 0xFF90A4AE)
        ).forEach { (name, local, color) ->
            dao.insertCategory(CategoryEntity(monthId = monthId, name = name, localizedName = local, icon = "category", colorHex = color, limitAmount = "0.00"))
        }
    }

    private suspend fun ensureMonthEditable(monthId: Long) {
        check(dao.month(monthId)?.readOnly == false) { "Month is read-only" }
    }
}

private fun com.everybirr.data.db.BudgetMonthEntity.toDomain() = BudgetMonth(id, monthKey, readOnly)
private fun CategoryEntity.toDomain() = Category(id, monthId, name, localizedName, icon, colorHex, limitAmount.toBigDecimal(), hidden)
private fun ExpenseEntity.toDomain() = Expense(id, monthId, categoryId, amount.toBigDecimal(), memo, createdAt)
private fun Category.toEntity() = CategoryEntity(id = id, monthId = monthId, name = name, localizedName = localizedName, icon = icon, colorHex = colorHex, limitAmount = limit.toPlainString(), hidden = hidden)
private fun Expense.toEntity() = ExpenseEntity(id = id, monthId = monthId, categoryId = categoryId, amount = amount.toPlainString(), memo = memo, createdAt = createdAt)
