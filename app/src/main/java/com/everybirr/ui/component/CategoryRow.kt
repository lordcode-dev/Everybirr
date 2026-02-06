package com.everybirr.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.everybirr.core.OverspendingState
import com.everybirr.core.BudgetMath
import com.everybirr.domain.model.CategoryBudget

@Composable
fun CategoryRow(categoryBudget: CategoryBudget) {
    val state = BudgetMath.overspendingState(categoryBudget.category.limit, categoryBudget.spent)
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(categoryBudget.category.name)
            Text("${categoryBudget.available}")
        }
        LinearProgressIndicator(
            progress = {
                if (categoryBudget.category.limit.signum() == 0) 0f else categoryBudget.spent.divide(categoryBudget.category.limit, 4, java.math.RoundingMode.HALF_UP).toFloat().coerceIn(0f,1f)
            }
        )
        if (state != OverspendingState.SAFE) {
            Text(if (state == OverspendingState.WARNING) "⚠ Near limit" else "⛔ Exceeded")
        }
    }
}
