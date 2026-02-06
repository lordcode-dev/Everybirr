package com.everybirr.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BudgetMonthEntity::class, IncomeEntity::class, CategoryEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
}
