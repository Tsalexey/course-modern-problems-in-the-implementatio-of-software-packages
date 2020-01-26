package ru.rudn.science.belov_course.task6

import ru.rudn.science.belov_course.Utils
import kotlin.math.pow

object Conditions {
    val N = 100
    val M = 100

    val x = 0..1
    val t = 0..1

    val h: Double = (x.last.toDouble() - x.first.toDouble()) / N
    val tau: Double = (t.last.toDouble() - t.first.toDouble()) / M

    val times = listOf<Double>(0.0, 0.25, 0.5, 0.75, 1.0)
}

fun main(args: Array<String>) {
    explicit(Conditions.x, Conditions.t, Conditions.N, Conditions.M, Conditions.times)
    implicit(Conditions.x, Conditions.t, Conditions.N, Conditions.M, Conditions.times)
}

fun f(x: Double, t: Double): Double = 0.0

fun applyCondition(u: Map<Int, MutableMap<Int, Double>>, m: Int, n: Int) {
    u.getValue(m).set(n, Double.NaN)
    if (n == 0) (u.getValue(m).put(n, 0.0))
    if (n == Conditions.N) (u.getValue(m).put(n, 1.0))
    if (m == 0) u.getValue(m).put(n, Utils.point(Conditions.x, n, Conditions.h).pow(2))
    return;
}

fun explicit(x: IntRange, t: IntRange, N: Int, M: Int, times: List<Double>) {
    println("explicit")

    val h: Double = Conditions.h
    val tau: Double = Conditions.tau

    val u = Utils.init(0..N, 0..M, ::applyCondition)

    println("h=${h}, tau=${tau}, R=${tau / h.pow(2)}")

    (0..M - 1).forEach { m ->
        println("m=${m}/${M - 1}")
        (0..N - 2).forEach { n ->
            val xPoint = Utils.point(x, n, h)
            val tPoint = Utils.point(t, m, tau)
            val phi = f(xPoint, tPoint)

            val u_np1_m = u.getValue(m).getValue(n + 1)
            val u_n_m = u.getValue(m).getValue(n)
            val u_np2_m = u.getValue(m).getValue(n + 2)

            u.getValue(m + 1).put(n + 1, (tau * (u_np2_m - 2 * u_np1_m - u_n_m) + tau * h.pow(2) * phi + h.pow(2) * u_np1_m / h.pow(2)))
        }
    }

    Utils.saveU(u, "task6_data", "task6_explicit", 0..N, 0..M, x, h, t, tau, times)
}

fun calculateD(u: Map<Int, MutableMap<Int, Double>>, n: Int, m: Int, x: IntRange, t: IntRange, h: Double, tau: Double, sigma: Double): Double {
    return -tau * h.pow(2) * f(Utils.point(x, n, h), Utils.point(t, m, tau)) - h.pow(2) * u.getValue(m).getValue(n)
}

fun implicit(x: IntRange, t: IntRange, N: Int, M: Int, times: List<Double>) {
    println("implicit");

    val h: Double = Conditions.h
    val tau: Double = Conditions.h

    val u = Utils.init(0..N, 0..M, ::applyCondition)

    (0..M - 1).forEach { m ->
        println("m=${m}/${M}");
        var A: Double = tau
        var B: Double = -(h.pow(2) + 2 * tau)
        var C: Double = tau

        Utils.thomasMethod(A, B, C, ::calculateD, u, m, N, x, t, h, tau, 0.0);
    }

    Utils.saveU(u, "task6_data", "task6_implicit", 0..N, 0..M, x, h, t, tau, times)
}
