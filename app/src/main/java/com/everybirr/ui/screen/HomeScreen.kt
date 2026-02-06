package com.everybirr.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.everybirr.ui.component.CategoryRow
import com.everybirr.ui.viewmodel.AppViewModel

@Composable
fun HomeScreen(vm: AppViewModel) {
    val state by vm.uiState.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.fastAddExpense() }) { Text("+") }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Income: ${state.summary.totalIncome}")
            Text("Spent: ${state.summary.totalSpent}")
            Text("Remaining: ${state.summary.totalRemaining}")
            LazyColumn {
                items(state.categories) { CategoryRow(it) }
            }
        }
    }
}
