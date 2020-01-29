package ru.rudn.science.belov_course.task7

import ru.rudn.science.belov_course.Utils
import kotlin.math.exp
import kotlin.math.pow

object Conditions {
    val N = 100
    val M = 100

    val x = -1..1
    val t = 0..2

    val h: Double = (x.last.toDouble() - x.first.toDouble()) / N
    val tau: Double = (t.last.toDouble() - t.first.toDouble()) / M
    val sigma = 0.05

    val times = listOf<Double>(0.0, 0.5, 1.0, 1.5, 2.0)
}

fun main(args: Array<String>) {
    implicit(Conditions.x, Conditions.t, Conditions.N, Conditions.M, Conditions.times)
}

fun applyCondition(u: Map<Int, MutableMap<Int, Double>>, m: Int, n: Int) {
    u.getValue(m).set(n, Double.NaN)
    if (n == 0) (u.getValue(m).put(n, exp(-2.0)))
    if (n == Conditions.N) (u.getValue(m).put(n, exp(-1.0)))
    if (m == 0) u.getValue(m).put(n, exp(-2.0))
    if (m == 1) u.getValue(m).put(n, u.getValue(0).getValue(n))
    return;
}

fun calculateD(u: Map<Int, MutableMap<Int, Double>>, n: Int, m: Int, x: IntRange, t: IntRange, h: Double, tau: Double, sigma: Double): Double {
    val u_nm1_mm1 = u.getValue(m - 1).getValue(n - 1)
    val u_n_mm1 = u.getValue(m - 1).getValue(n)
    val u_np1_mm1 = u.getValue(m - 1).getValue(n + 1)
    val u_nm1_m = u.getValue(m).getValue(n - 1)
    val u_n_m = u.getValue(m).getValue(n)
    val u_np1_m = u.getValue(m).getValue(n + 1)

    val G = sigma * (u_nm1_mm1 - 2 * u_n_mm1 + u_np1_mm1) / (h.pow(2)) + ((1 - 2 * sigma) * (u_nm1_m - 2 * u_n_m + u_np1_m)) / (h.pow(2))
    return -(tau.pow(2) * G + 2 * u_n_m - u_n_mm1)
}

fun implicit(x: IntRange, t: IntRange, N: Int, M: Int, times: List<Double>) {
    println("implicit");

    val h: Double = Conditions.h
    val tau: Double = Conditions.tau
    val sigma = Conditions.sigma

    val u = Utils.init(0..N, 0..M, ::applyCondition)

    (1..M - 1).forEach { m ->
        println("m=${m}/${M}");
        var A: Double = sigma * tau.pow(2) / h.pow(2)
        var B: Double = -(1 + (2 * sigma * tau.pow(2)) / (h.pow(2)))
        var C: Double = (sigma * tau.pow(2)) / h.pow(2)
        Utils.thomasMethod(A, B, C, ::calculateD, u, m, N, x, t, h, tau, sigma);
    }

    Utils.saveU(u, "task7_data", "task7_implicit", 0..N, 0..M, x, h, t, tau, times)
}




