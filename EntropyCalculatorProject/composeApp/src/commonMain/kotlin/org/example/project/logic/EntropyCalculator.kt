package org.example.project.logic

import org.example.project.model.EntropyResult
import org.example.project.model.OutcomeEntry
import kotlin.math.abs
import kotlin.math.log2

object EntropyCalculator {

    // H(λ) = Σ(i=1..k) [ -p(Ai) * log2(p(Ai)) ]
    fun calculate(outcomes: List<OutcomeEntry>): EntropyResult {
        require(outcomes.isNotEmpty()) { "Список исходов пуст" }

        val sum = outcomes.sumOf { it.probability }
        require(abs(sum - 1.0) < 1e-6) {
            "Σp(Ai) = ${"%.4f".format(sum)}, должна быть = 1.0"
        }

        // Каждое слагаемое: -p(Ai) * log2(p(Ai))
        // Если p == 0, слагаемое = 0 (предел p*log(p) = 0 при p = 0)
        val h = -outcomes.sumOf { (_, p) ->
            if (p <= 0.0) 0.0 else -p * log2(p)
        }

        val growth = buildGrowthCurve(maxOf(outcomes.size, 12))

        return EntropyResult(
            entropy = h,
            outcomes = outcomes,
            growthPoints = growth
        )
    }

    // Каждое из k слагаемых: -(1/k) * log2(1/k) = (1/k) * log2(k)
    // H = k * (1/k) * log2(k) = log2(k)
    fun buildGrowthCurve(maxK: Int): List<Pair<Int, Double>> {
        return (1..maxK).map { k ->
            val p = 1.0 / k
            // Σ(i=1..k) [ -p * log2(p) ] = k * (-p * log2(p))
            val h = k * (-p * log2(p))
            k to h
        }
    }
}