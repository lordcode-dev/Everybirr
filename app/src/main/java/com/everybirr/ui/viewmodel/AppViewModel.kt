package com.everybirr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everybirr.domain.model.Expense
import com.everybirr.domain.model.MonthSummary
import com.everybirr.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

data class AppUiState(
    val summary: MonthSummary = MonthSummary(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
    val categories: List<com.everybirr.domain.model.CategoryBudget> = emptyList(),
    val activeMonthId: Long? = null
)

@HiltViewModel
class AppViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun ensureCurrentMonth() {
        viewModelScope.launch {
            val active = repository.activeMonth().firstOrNull()
            val monthId = active?.id ?: repository.createMonth(YearMonth.now().toString(), null)
            bindMonth(monthId)
        }
    }

    private suspend fun bindMonth(monthId: Long) {
        repository.setActiveMonth(monthId)
        _uiState.value = _uiState.value.copy(activeMonthId = monthId)
        viewModelScope.launch {
            repository.monthSummary(monthId).collect { summary ->
                _uiState.value = _uiState.value.copy(summary = summary)
            }
        }
        viewModelScope.launch {
            repository.categoryBudgets(monthId).collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories)
            }
        }
    }

    fun fastAddExpense() {
        viewModelScope.launch {
            val monthId = _uiState.value.activeMonthId ?: return@launch
            val category = _uiState.value.categories.firstOrNull() ?: return@launch
            repository.addExpense(Expense(0, monthId, category.category.id, BigDecimal("10.00"), "Fast add", System.currentTimeMillis()))
        }
    }
}

