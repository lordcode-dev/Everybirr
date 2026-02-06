package com.everybirr

import com.everybirr.domain.model.BudgetMonth
import com.everybirr.domain.model.Category
import com.everybirr.domain.model.CategoryBudget
import com.everybirr.domain.model.Expense
import com.everybirr.domain.model.MonthSummary
import com.everybirr.domain.repository.BudgetRepository
import com.everybirr.ui.viewmodel.AppViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class ViewModelTest {
    @Test
    fun fastAddExpense_updatesThroughRepo() = runBlocking {
        val repo = FakeRepo()
        val vm = AppViewModel(repo)
        vm.ensureCurrentMonth()
        vm.fastAddExpense()
        assertTrue(repo.addedExpense)
    }
}

private class FakeRepo : BudgetRepository {
    var addedExpense = false
    private val active = MutableStateFlow(BudgetMonth(1, "2025-01", false))
    override fun activeMonth(): Flow<BudgetMonth?> = active
    override fun monthSummary(monthId: Long): Flow<MonthSummary> = flowOf(MonthSummary(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE))
    override fun categoryBudgets(monthId: Long): Flow<List<CategoryBudget>> = flowOf(listOf(CategoryBudget(Category(1,1,"Food",null,"",0,BigDecimal.TEN,false), BigDecimal.ZERO, BigDecimal.TEN)))
    override fun expenses(categoryId: Long): Flow<List<Expense>> = flowOf(emptyList())
    override suspend fun createMonth(monthKey: String, copyFromMonthId: Long?): Long = 1
    override suspend fun setActiveMonth(monthId: Long) {}
    override suspend fun addIncome(monthId: Long, name: String, amount: BigDecimal) {}
    override suspend fun allocateCategory(category: Category) {}
    override suspend fun addExpense(expense: Expense) { addedExpense = true }
    override suspend fun setMonthReadOnly(monthId: Long, readOnly: Boolean) {}
    override suspend fun exportCsv(monthId: Long): String = ""
    override suspend fun importDefaultsIfNeeded(monthId: Long) {}
}
