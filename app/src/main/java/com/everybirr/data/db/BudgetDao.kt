package com.everybirr.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM months WHERE isActive = 1 LIMIT 1")
    fun activeMonth(): Flow<BudgetMonthEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonth(entity: BudgetMonthEntity): Long

    @Query("UPDATE months SET isActive = CASE WHEN id = :monthId THEN 1 ELSE 0 END")
    suspend fun setActiveMonth(monthId: Long)

    @Query("SELECT * FROM months WHERE id = :monthId")
    suspend fun month(monthId: Long): BudgetMonthEntity?

    @Query("UPDATE months SET readOnly = :readOnly WHERE id = :monthId")
    suspend fun setMonthReadOnly(monthId: Long, readOnly: Boolean)

    @Query("SELECT * FROM categories WHERE monthId = :monthId")
    fun categories(monthId: Long): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(entity: CategoryEntity): Long

    @Query("SELECT * FROM categories WHERE monthId = :monthId")
    suspend fun categoriesNow(monthId: Long): List<CategoryEntity>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY createdAt DESC")
    fun expenses(categoryId: Long): Flow<List<ExpenseEntity>>


    @Query("SELECT * FROM expenses WHERE monthId = :monthId")
    fun expensesByMonthFlow(monthId: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE monthId = :monthId")
    suspend fun expensesNow(monthId: Long): List<ExpenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(entity: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(entity: IncomeEntity)

    @Query("SELECT * FROM income_sources WHERE monthId = :monthId")
    fun incomes(monthId: Long): Flow<List<IncomeEntity>>

    @Query("SELECT * FROM income_sources WHERE monthId = :monthId")
    suspend fun incomesNow(monthId: Long): List<IncomeEntity>
}
