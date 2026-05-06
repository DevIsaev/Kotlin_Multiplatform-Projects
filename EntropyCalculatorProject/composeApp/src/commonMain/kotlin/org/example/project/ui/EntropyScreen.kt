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
import org.example.project.ui.charts.BarChart
import org.example.project.ui.charts.LineChart

@Composable
fun EntropyScreen(vm: EntropyViewModel = remember { EntropyViewModel() }) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Заголовок
        Text(
            text = "Вычисление энтропии H(λ)",
            fontSize = 22.sp,
            modifier = Modifier.padding(top=20.dp),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "H(λ) = Σ -p(Aᵢ) · log2(p(Aᵢ))",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider()

        // Ошибка
        vm.errorMessage?.let { msg ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "⚠ $msg",
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Ввод N
        Text("Шаг 1: Укажите количество исходов N", fontWeight = FontWeight.SemiBold)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = vm.nInput,
                onValueChange = { vm.nInput = it },
                label = { Text("N") },
                singleLine = true,
                modifier = Modifier.width(100.dp)
            )
            Button(onClick = { vm.applyN() }) {
                Text("Применить")
            }
        }

        // Динамические строки исходов
        if (vm.nameFields.isNotEmpty()) {

            Text("Шаг 2: Введите исходы и вероятности", fontWeight = FontWeight.SemiBold)
            Text(
                text = "Совет: вероятности должны давать в сумме 1.0\n" +
                        "Пример для равномерного: каждая = ${
                            "%.4f".format(1.0 / vm.nameFields.size)
                        }",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            vm.nameFields.indices.forEach { i ->
                OutcomeRow(
                    index = i,
                    nameValue = vm.nameFields[i],
                    probValue = vm.probFields[i],
                    onNameChange = { vm.updateName(i, it) },
                    onProbChange = { vm.updateProb(i, it) }
                )
            }
        }

        // Кнопка расчёта
        Button(
            onClick = { vm.calculate() },
            enabled = vm.nameFields.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать H(λ)", fontSize = 16.sp)
        }

        // Результат
        vm.result?.let { res ->

            HorizontalDivider()

            // Значение энтропии
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Результат:", fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "H(λ) = ${"%.6f".format(res.entropy)} бит",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Максимально возможная (равномерная): " +
                                "${"%.6f".format(Math.log(res.outcomes.size.toDouble()) / Math.log(2.0))} бит",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Таблица исходов
            Text("Таблица исходов:", fontWeight = FontWeight.SemiBold)
            OutcomeTable(res.outcomes)

//            // График 1: гистограмма вероятностей
//            Text("График вероятностей p(Aᵢ):", fontWeight = FontWeight.SemiBold)
//            BarChart(
//                labels = res.outcomes.map { it.name },
//                values = res.outcomes.map { it.probability.toFloat() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(220.dp)
//            )
//
//            // График 2: кривая роста энтропии
//            Text(
//                "Рост H(λ) при равномерном распределении (N от 1 до ${res.growthPoints.size}):",
//                fontWeight = FontWeight.SemiBold
//            )
//            LineChart(
//                points = res.growthPoints.map { (k, h) -> k.toFloat() to h.toFloat() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(220.dp)
//            )
//        }
//
//        Spacer(Modifier.height(24.dp))
    }
}