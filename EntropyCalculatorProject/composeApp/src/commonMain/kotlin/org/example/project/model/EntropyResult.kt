package org.example.project.model

data class EntropyResult(
    val entropy: Double,
    val outcomes: List<OutcomeEntry>,
    val growthPoints: List<Pair<Int, Double>> // (N, H) для равномерного
)