package com.everybirr

import com.everybirr.data.db.BudgetDao
import com.everybirr.data.db.BudgetMonthEntity
import com.everybirr.data.db.CategoryEntity
import com.everybirr.data.db.ExpenseEntity
import com.everybirr.data.db.IncomeEntity
import com.everybirr.data.repository.BudgetRepositoryImpl
import com.everybirr.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class MonthRulesTest {
    @Test(expected = IllegalStateException::class)
    fun readOnlyMonthRejectsExpense() = runBlocking {
        val dao = FakeDao(readOnly = true)
        val repo = BudgetRepositoryImpl(dao)
        repo.addExpense(Expense(0, 1, 1, BigDecimal.ONE, "x", 1L))
    }

    @Test
    fun copyMonthCopiesCategories() = runBlocking {
        val dao = FakeDao(readOnly = false)
        dao.seedCategory(CategoryEntity(id = 3, monthId = 2, name = "Food", localizedName = null, icon = "i", colorHex = 0, limitAmount = "100", hidden = false))
        val repo = BudgetRepositoryImpl(dao)
        repo.createMonth("2025-01", 2)
        assertEquals(1, dao.categoriesNow(1).size)
    }
}

private class FakeDao(private val readOnly: Boolean) : BudgetDao {
    private val active = MutableStateFlow(BudgetMonthEntity(id = 1, monthKey = "2025-01", readOnly = readOnly, isActive = true))
    private val categories = mutableListOf<CategoryEntity>()
    override fun activeMonth(): Flow<BudgetMonthEntity?> = active
    override suspend fun insertMonth(entity: BudgetMonthEntity): Long = 1
    override suspend fun setActiveMonth(monthId: Long) {}
    override suspend fun month(monthId: Long): BudgetMonthEntity? = active.value
    override suspend fun setMonthReadOnly(monthId: Long, readOnly: Boolean) {}
    override fun categories(monthId: Long): Flow<List<CategoryEntity>> = flowOf(categories.filter { it.monthId == monthId })
    override suspend fun insertCategory(entity: CategoryEntity): Long { categories += entity.copy(id = categories.size.toLong()+1); return categories.size.toLong() }
    override suspend fun categoriesNow(monthId: Long): List<CategoryEntity> = categories.filter { it.monthId == monthId }
    override fun expenses(categoryId: Long): Flow<List<ExpenseEntity>> = flowOf(emptyList())
    override fun expensesByMonthFlow(monthId: Long): Flow<List<ExpenseEntity>> = flowOf(emptyList())
    override suspend fun expensesNow(monthId: Long): List<ExpenseEntity> = emptyList()
    override suspend fun insertExpense(entity: ExpenseEntity) {}
    override suspend fun insertIncome(entity: IncomeEntity) {}
    override fun incomes(monthId: Long): Flow<List<IncomeEntity>> = flowOf(emptyList())
    override suspend fun incomesNow(monthId: Long): List<IncomeEntity> = emptyList()
    fun seedCategory(entity: CategoryEntity) { categories += entity }
}
