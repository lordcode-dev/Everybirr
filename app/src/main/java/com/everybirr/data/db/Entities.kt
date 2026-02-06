package com.everybirr.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "months", indices = [Index("monthKey", unique = true)])
data class BudgetMonthEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val monthKey: String,
    val readOnly: Boolean = false,
    val isActive: Boolean = false
)

@Entity(
    tableName = "income_sources",
    foreignKeys = [ForeignKey(entity = BudgetMonthEntity::class, parentColumns = ["id"], childColumns = ["monthId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("monthId")]
)
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val monthId: Long,
    val name: String,
    val amount: String
)

@Entity(
    tableName = "categories",
    foreignKeys = [ForeignKey(entity = BudgetMonthEntity::class, parentColumns = ["id"], childColumns = ["monthId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("monthId")]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val monthId: Long,
    val name: String,
    val localizedName: String?,
    val icon: String,
    val colorHex: Long,
    val limitAmount: String,
    val hidden: Boolean = false
)

@Entity(
    tableName = "expenses",
    foreignKeys = [ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["categoryId"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index("monthId"), Index("categoryId")]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val monthId: Long,
    val categoryId: Long,
    val amount: String,
    val memo: String,
    val createdAt: Long
)
