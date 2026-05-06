package org.example.project.ui

import androidx.compose.runtime.*
import org.example.project.logic.EntropyCalculator
import org.example.project.model.EntropyResult
import org.example.project.model.OutcomeEntry

class EntropyViewModel {

    // Количество исходов
    var nInput by mutableStateOf("4")

    // Динамические поля
    var nameFields by mutableStateOf(listOf<String>())
    var probFields by mutableStateOf(listOf<String>())

    var result by mutableStateOf<EntropyResult?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        applyN()
    }

    // Создаем нужное количество строк ввода
         fun applyN() {
        val n = nInput.trim().toIntOrNull()
        if (n == null || n < 1 || n > 50) {
            errorMessage = "N должно быть целым числом от 1 до 50"
            return
        }
        errorMessage = null

        // Сохраняем старые значения если они были
        val oldNames = nameFields
        val oldProbs = probFields

        nameFields = List(n) { i -> oldNames.getOrElse(i) { "A${i + 1}" } }
        probFields = List(n) { i -> oldProbs.getOrElse(i) { "" } }
        result = null
    }

    fun updateName(index: Int, value: String) {
        nameFields = nameFields.toMutableList().also { it[index] = value }
    }

    fun updateProb(index: Int, value: String) {
        probFields = probFields.toMutableList().also { it[index] = value }
    }

    // Парсинг введённых данных и просчет энтропии
    fun calculate() {
        errorMessage = null
        result = null

        try {
            val outcomes = nameFields.zip(probFields).mapIndexed { i, (name, prob) ->
                val p = prob.trim().replace(",", ".").toDoubleOrNull()
                    ?: throw IllegalArgumentException(
                        "Некорректная вероятность для ${name.ifBlank { "A${i + 1}" }}: \"$prob\""
                    )
                OutcomeEntry(name = name.ifBlank { "A${i + 1}" }, probability = p)
            }

            result = EntropyCalculator.calculate(outcomes)

        } catch (e: Exception) {
            errorMessage = e.message ?: "Неизвестная ошибка"
        }
    }
}