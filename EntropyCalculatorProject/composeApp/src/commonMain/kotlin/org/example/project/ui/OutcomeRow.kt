package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Строка ввода одного исхода
@Composable
fun OutcomeRow(
    index: Int,
    nameValue: String,
    probValue: String,
    onNameChange: (String) -> Unit,
    onProbChange: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${index + 1}.",
            modifier = Modifier.width(24.dp),
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = nameValue,
            onValueChange = onNameChange,
            label = { Text("Название") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = probValue,
            onValueChange = onProbChange,
            label = { Text("p(Aᵢ)") },
            singleLine = true,
            modifier = Modifier.weight(1f),
            placeholder = { Text("0.25") }
        )
    }
}

// Таблица исходов
@Composable
fun OutcomeTable(outcomes: List<org.example.project.model.OutcomeEntry>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Заголовок
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Исход", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("p(Aᵢ)", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("−p·log₂p", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            outcomes.forEach { outcome ->
                val p = outcome.probability
                val term = if (p <= 0.0) 0.0 else -p * Math.log(p) / Math.log(2.0)
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(outcome.name, modifier = Modifier.weight(1f))
                    Text("%.4f".format(p), modifier = Modifier.weight(1f))
                    Text("%.4f".format(term), modifier = Modifier.weight(1f))
                }
            }
        }
    }
}